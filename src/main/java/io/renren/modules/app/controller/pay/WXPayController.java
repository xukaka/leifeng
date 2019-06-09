package io.renren.modules.app.controller.pay;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.*;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.*;
import io.renren.modules.app.entity.TaskStatusEnum;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.*;
import io.renren.modules.app.service.impl.WXPayService;
import io.renren.modules.app.utils.ReqUtils;
import io.renren.modules.app.utils.WXPayConstants;
import io.renren.modules.app.utils.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/wx/pay")
@Api(tags = "微信支付")
public class WXPayController {
    private final static Logger logger = LoggerFactory.getLogger(WXPayController.class);
    @Autowired
    private WXPayService wxPayService;
    @Autowired
    private TaskOrderService taskOrderService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WithdrawalOrderService withdrawalOrderService;
    @Autowired
    private MemberWalletService memberWalletService;

    @Autowired
    private MemberWalletLogService memberWalletLogService;

    @Login
    @PostMapping("/prePay")
    @ApiOperation("微信预下订单接口")
    public R prePay( Long totalFee, Long taskId) throws Exception {
        MemberWalletEntity wallet= memberWalletService.selectOne(new EntityWrapper<MemberWalletEntity>()
                .eq("member_id", ReqUtils.curMemberId()));

        String prodDesc="任务订单";
        //生成taskorder订单入库
        String outTradeNo;
        TaskOrderEntity taskOrder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (taskOrder == null) { //新增
            TaskOrderEntity torder = new TaskOrderEntity();
            outTradeNo= "NO" + OrderNoUtil.generateOrderNo(taskId);
            torder.setOutTradeNo(outTradeNo);
            torder.setAttach(prodDesc);
            torder.setTaskId(taskId);
            torder.setTotalFee(totalFee);
            torder.setCreateTime(DateUtils.now());
            taskOrderService.insert(torder);
        }else{
            outTradeNo= taskOrder.getOutTradeNo();
        }
        String totalFeeStr = String.valueOf(totalFee);//交易的金额，单位为分
        Map<String, String> reqData = wxPayService.fillRequestData(prodDesc, outTradeNo, totalFeeStr, ReqUtils.getRemoteAddr(), wallet.getOpenId());
        logger.info("微信预下订单请求参数：{}", JsonUtil.Java2Json(reqData));
        String wxResponse = wxPayService.prePayRequest(WXPayUtil.mapToXml(reqData));
        logger.info("微信预下订单请求结果：{}", wxResponse);
        Map<String, String> wxPayMap = wxPayService.reGenerateParamForApp(wxResponse);
        return R.ok().put("result", wxPayMap);
    }

    /**
     * 回调内容示例：
     * {transaction_id=4200000297201904277527344712, nonce_str=BST4QgtRJRH13YV0BF0qhyU3Ohvjswqm, bank_type=CFT, openid=o7rvm5aoVJtrPPWQAapMxc_cmU50, sign=E2D2EAC8F45A8FE7A66F1BF6CD6AA30A, fee_type=CNY, mch_id=1517937991, cash_fee=1, out_trade_no=NO20190427152013568, appid=wxf303172e683e60d0, total_fee=1, trade_type=JSAPI, result_code=SUCCESS, time_end=20190427152056, is_subscribe=N, return_code=SUCCESS}
     */
    @PostMapping("/notify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("[WXPayController.wxNotify] 进入");
        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuilder sb = new StringBuilder();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());
        logger.info("微信回调的内容为：{}", map);

        if (WXPayConstants.SUCCESS.equals(map.get("return_code"))) {
            //签名校验
            boolean signFlag = wxPayService.validateSign(map);
            if (WXPayConstants.SUCCESS.equals(map.get("result_code")) && signFlag) {
                String outTradeNo = map.get("out_trade_no");
                long totalFee = Long.valueOf(map.get("total_fee"));
                TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("out_trade_no", outTradeNo));
                Long orderTotalFee = torder.getTotalFee();
                logger.info("根据单号获取到系统订单为：{}", JsonUtil.Java2Json(torder));
                if (orderTotalFee != null && orderTotalFee.intValue() == totalFee) {
                    torder.setTransactionId(map.get("transaction_id"));
                    torder.setTimeEnd(map.get("time_end"));
                    torder.setTradeState(WXPayConstants.SUCCESS);
                    taskOrderService.updateById(torder);
                    //发布任务
                    taskService.publishTask(torder.getTaskId());

                    //返回微信，已接收到结果
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    Map<String, String> returnCode = new HashMap<>();
                    returnCode.put("return_code", WXPayConstants.SUCCESS);
                    out.write(WXPayUtil.mapToXml(returnCode).getBytes());
                    out.flush();
                    out.close();
                    logger.info("成功返回结果给微信");
                }
            }
        }

    }


    //小程序调用接口回查任务订单支付结果
    @PostMapping("/orderQuery")
    @ApiOperation("程序调用接口回查任务订单支付结果")
    public R orderQuery(Long taskId) throws Exception {
        logger.info("[WXPayController.orderQuery] 进入");
        TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        logger.info("根据taskid查询到订单：{}", JsonUtil.Java2Json(torder));
        if (WXPayConstants.SUCCESS.equals(torder.getTradeState())) {
            return R.ok(WXPayConstants.SUCCESS);
        } else {
            //从微信平台查询订单支付状态
            String xresdata = wxPayService.orderQueryRequest(torder.getOutTradeNo());
            Map<String, String> map = WXPayUtil.xmlToMap(xresdata);
            if (WXPayConstants.SUCCESS.equals(map.get("return_code")) && WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
                torder.setTradeState(map.get("trade_state"));
                taskOrderService.updateById(torder);
                return R.ok(map.get("trade_state"));
            }
            return R.error();
        }
    }

    //企业转账提现功能接口
    /*@Login
    @PostMapping("/transfer")
    @ApiOperation("企业提现功能接口")
    public R transferMoney(String openId, String realName, Integer amount, Long memberId) throws Exception {
        logger.info("[WXPayController.transferMoney] 进入");
        String transdata = wxPayService.transferMoneyRequest(openId, realName, String.valueOf(amount));
        logger.info("转账提现接口微信返回结果：{}", transdata);

        Map<String, String> map = WXPayUtil.xmlToMap(transdata);
        String returnCode = map.get("return_code");
        if (!StringUtils.isEmpty(returnCode) && "SUCCESS".equals(returnCode)) {
            String resultCode = map.get("result_code");
            if (!StringUtils.isEmpty(resultCode) && "SUCCESS".equals(resultCode)) {

                return R.ok().put("tradeNo", map.get("partner_trade_no"))
                        .put("paymentNo", map.get("payment_no"))
                        .put("paymentTime", map.get("payment_time"));
            } else {
                logger.info(map.get("err_code") + ":" + map.get("err_code_des"));
                return R.error(map.get("err_code") + ":" + map.get("err_code_des"));
            }
        } else {
            logger.info(map.get("return_msg"));
            return R.error(map.get("return_msg"));
        }
    }*/

    //申请退款接口
    @Login
    @PostMapping("/refund")
    @ApiOperation("申请退款接口")
    public R refund(Long taskId) throws Exception {
        logger.info("[WXPayController.refund] 进入 request param taskId=" + taskId);
        TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (torder == null) {
            return R.error("任务订单未生成");
        }
        if (torder.getTotalFee() <= 0) {
            return R.error("任务订单金额为0");
        }
        if (!WXPayConstants.SUCCESS.equals(torder.getTradeState())) {
            return R.error("任务订单状态异常：TradeState="+torder.getTradeState());
        }
 /*       TaskEntity task = taskService.selectById(taskId);
        if (task.getStatus()!=TaskStatusEnum)*/

        String refundData = wxPayService.refundRequest(torder.getTransactionId(), taskId, String.valueOf(torder.getTotalFee()));
        logger.info("退款接口微信返回结果：{}", refundData);

        Map<String, String> map = WXPayUtil.xmlToMap(refundData);
        if (WXPayConstants.SUCCESS.equals(map.get("return_code"))) {
            if (WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
                torder.setTradeState(WXPayConstants.REFUND);
                taskOrderService.updateById(torder);

                return R.ok().put("refundId", map.get("refund_id"))
                        .put("refundFee", map.get("refund_fee"));
            } else {
                logger.info(map.get("err_code") + ":" + map.get("err_code_des"));
                return R.error(map.get("err_code") + ":" + map.get("err_code_des"));
            }
        } else {
            logger.info(map.get("return_msg"));
            return R.error(map.get("return_msg"));
        }


    }

    //企业提现订单
    @Login
    @PostMapping("/preWithdrawal")
    @ApiOperation("提现订单申请接口")
    public R preWithdrawal(Long amount) {
        wxPayService.preWithdrawal(ReqUtils.curMemberId(), amount);
        return R.ok();
    }


    //企业提现功能接口
    @GetMapping("/withdrawal")
    @ApiOperation("提现功能接口")
    public R withdrawal(String outTradeNo) throws Exception {
        logger.info("[WXPayController.withdrawal] 进入");
        Map<String, String> result = wxPayService.withdrawal(outTradeNo);
        return R.ok().put("result", result);
    }

    @GetMapping("/logs")
    @ApiOperation("分页获取钱包交易日志列表")
    public R getLogs(Long memberId,Integer curPage, Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<MemberWalletLogDto> logs = memberWalletLogService.getLogs(memberId,page);
        return R.ok().put("result", logs);
    }

    @GetMapping("/checkTotalMoney")
    @ApiOperation("校验用户交易数据")
    public R checkTotalMoney(Long memberId) {
        MoneyCheckDto check = memberWalletLogService.checkTotalMoney(memberId);
        return R.ok().put("result", check);
    }

    @GetMapping("/withdrawal/list")
    @ApiOperation("分页获取企业提现订单列表")
    public R getWithdrawalOrders(Integer curPage, Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<WithdrawalOrderDto> orders = withdrawalOrderService.getWithdrawalOrders(page);
        return R.ok().put("result", orders);
    }



    @GetMapping("/withdrawal/check")
    @ApiOperation("校验提现用户的交易数据是否正常")
    public R checkWithdrawalOrder(Long id) {
        withdrawalOrderService.checkWithdrawalOrder(id);
        return R.ok();
    }


    @GetMapping("/list")
    @ApiOperation("分页获取任务订单列表")
    @Transactional
    public R getTaskOrders(String tradeState, Integer curPage, Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<TaskOrderDto> orders = taskOrderService.getTaskOrders(tradeState, page);
        return R.ok().put("result", orders);
    }


    @Login
    @GetMapping("/wallet")
    @ApiOperation("获取用户钱包信息")
    public R getWallet() {
        MemberWalletDto wallet = memberWalletService.getWallet(ReqUtils.curMemberId());
        return R.ok().put("result", wallet);
    }

}
