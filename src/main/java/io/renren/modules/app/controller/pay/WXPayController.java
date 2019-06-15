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
    public R prePay(Long totalFee, Long taskId) throws Exception {
        MemberWalletEntity wallet = memberWalletService.selectOne(new EntityWrapper<MemberWalletEntity>()
                .eq("member_id", ReqUtils.curMemberId()));

        String prodDesc = "任务订单";
        //生成taskorder订单入库
        String outTradeNo;
        TaskOrderEntity taskOrder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (taskOrder == null) { //新增
            TaskOrderEntity torder = new TaskOrderEntity();
            outTradeNo = "NO" + OrderNoUtil.generateOrderNo(taskId);
            torder.setOutTradeNo(outTradeNo);
            torder.setAttach(prodDesc);
            torder.setTaskId(taskId);
            torder.setTotalFee(totalFee);
            torder.setCreateTime(DateUtils.now());
            taskOrderService.insert(torder);
        } else {
            outTradeNo = taskOrder.getOutTradeNo();
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
    @GetMapping("/orderQuery")
    @ApiOperation("程序调用接口回查任务订单支付结果")
    public R orderQuery(Long taskOrderId) throws Exception {
        logger.info("[WXPayController.orderQuery] 进入");
//        TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        TaskOrderEntity torder = taskOrderService.selectById(taskOrderId);
        logger.info("根据taskid查询到订单：{}", JsonUtil.Java2Json(torder));
        //从微信平台查询订单支付状态
        String xresdata = wxPayService.orderQueryRequest(torder.getOutTradeNo());
        Map<String, String> map = WXPayUtil.xmlToMap(xresdata);
        if (WXPayConstants.SUCCESS.equals(map.get("return_code")) && WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
            return R.ok().put("result", map.get("trade_state"));
        }
        return R.error().put("result", map);


        /*if (WXPayConstants.SUCCESS.equals(torder.getTradeState())) {
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
        }*/
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
            return R.error("任务订单状态异常：TradeState=" + torder.getTradeState());
        }

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


    //退款查询接口
    @GetMapping("/refundQuery")
    @ApiOperation("查询微信退款结果")
    public R refundQuery(Long taskId) throws Exception {
        TaskOrderEntity taskOrder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (taskOrder == null) {
            return R.error().put("result", "task order is null,taskId=" + taskId);
        }
        logger.info("根据taskid查询到订单：{}", JsonUtil.Java2Json(taskOrder));
        //从微信平台查询退款信息
        String refundData = wxPayService.refundQueryRequest(taskOrder.getOutTradeNo());
        Map<String, String> map = WXPayUtil.xmlToMap(refundData);
        if (WXPayConstants.SUCCESS.equals(map.get("return_code")) && WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
            Map<String, String> refundMap = getRefundMap(map);

            return R.ok().put("result", refundMap);
        }
        return R.error().put("result", map);
    }

    /**
     * 封装退款信息
     * @param map
     * @return
     */
    private Map<String, String> getRefundMap(Map<String, String> map) {
        Map<String, String> refundMap = new HashMap<>();

        /**
         * 退款状态：
         * SUCCESS—退款成功
         * REFUNDCLOSE—退款关闭。
         * PROCESSING—退款处理中
         * CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。
         *
         * $n为下标，从0开始编号。
         */
        String refundStatus = null;
        if (map.get("result_code_0") != null) {
            refundStatus = map.get("result_code_0");
        } else if (map.get("result_code_1") != null) {
            refundStatus = map.get("result_code_1");
        } else if (map.get("result_code_2") != null) {
            refundStatus = map.get("result_code_2");
        } else if (map.get("result_code_3") != null) {
            refundStatus = map.get("result_code_3");
        }

        refundMap.put("refund_status", refundStatus);//退款状态
        /**
         * 退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
         */
        refundMap.put("settlement_refund_fee", map.get("settlement_refund_fee_0"));//退款金额
        /**
         * 退款成功时间，当退款状态为退款成功时有返回。$n为下标，从0开始编号。
         */
        refundMap.put("refund_success_time", map.get("refund_success_time_0"));//退款成功时间
        /**
         * 取当前退款单的退款入账方
         * 1）退回银行卡：
         * {银行名称}{卡类型}{卡尾号}
         * 2）退回支付用户零钱:
         * 支付用户零钱
         * 3）退还商户:
         * 商户基本账户
         * 商户结算银行账户
         * 4）退回支付用户零钱通:
         * 支付用户零钱通
         */
        String refundRecvAccout = null;
        if (map.get("refund_recv_accout_0") != null) {
            refundRecvAccout = map.get("refund_recv_accout_0");
        } else if (map.get("refund_recv_accout_1") != null) {
            refundRecvAccout = map.get("refund_recv_accout_0");
        } else if (map.get("refund_recv_accout_2") != null) {
            refundRecvAccout = map.get("refund_recv_accout_2");
        } else if (map.get("refund_recv_accout_3") != null) {
            refundRecvAccout = map.get("refund_recv_accout_3");
        }
        refundMap.put("refund_recv_accout", refundRecvAccout);//退款入账账户
        refundMap.put("refund_reason", "任务取消");//自定义
        return refundMap;
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
    public R getLogs(Long memberId, Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<MemberWalletLogDto> logs = memberWalletLogService.getLogs(memberId, page);
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
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<WithdrawalOrderDto> orders = withdrawalOrderService.getWithdrawalOrders(page);
        return R.ok().put("result", orders);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取任务订单列表")
    @Transactional
    public R getTaskOrders(String tradeState, Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
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
