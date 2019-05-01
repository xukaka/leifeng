package io.renren.modules.app.controller.pay;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mchange.lang.LongUtils;
import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.OrderNoUtil;
import io.renren.common.utils.R;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.service.TaskOrderService;
import io.renren.modules.app.service.impl.WXPayService;
import io.renren.modules.app.utils.ReqUtils;
import io.renren.modules.app.utils.WXPayConstants;
import io.renren.modules.app.utils.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
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

    @PostMapping("/prePay")
    @ApiOperation("微信预下订单接口")
    public R prePay(String prodDesc, float totalFee, String openid, Long taskId) throws Exception {
        logger.info("[WXPayController.prePay] request:prodDesc={},totleFee={},openid={}",prodDesc,totalFee,openid);
        Map<String,String> reqData = new HashMap<>();
        reqData.put("body",prodDesc); //商品描述
        reqData.put("out_trade_no", "NO"+OrderNoUtil.generateOrderNo(taskId));
        reqData.put("total_fee",String.valueOf((int)(totalFee*100))); //交易的金额，单位为分
        reqData.put("spbill_create_ip", ReqUtils.getRequest().getRemoteAddr());
        reqData.put("openid",openid);
        Map<String, String> reqDataComp = wxPayService.fillRequestData(reqData);
        logger.info("微信预下订单请求参数：{}", JsonUtil.Java2Json(reqDataComp));
        String wxResponse = wxPayService.prePayRequest(WXPayUtil.mapToXml(reqDataComp));
        logger.info("微信预下订单请求结果：{}",wxResponse);

        //生成taskorder订单入库
        int count = taskOrderService.selectCount(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (count==0){ //新增
            TaskOrderEntity torder = new TaskOrderEntity();
            torder.setOutTradeNo(reqData.get("out_trade_no"));
            torder.setAttach(reqData.get("body"));
            torder.setTaskId(taskId);
            torder.setTotalFee((long)(totalFee*100));
            taskOrderService.insert(torder);
        }
        Map<String, String> wxPayMap = wxPayService.reGenerateParamForApp(wxResponse);
        return R.ok().put("result", wxPayMap);
    }

    /**
     * 回调内容示例：
     *  {transaction_id=4200000297201904277527344712, nonce_str=BST4QgtRJRH13YV0BF0qhyU3Ohvjswqm, bank_type=CFT, openid=o7rvm5aoVJtrPPWQAapMxc_cmU50, sign=E2D2EAC8F45A8FE7A66F1BF6CD6AA30A, fee_type=CNY, mch_id=1517937991, cash_fee=1, out_trade_no=NO20190427152013568, appid=wxf303172e683e60d0, total_fee=1, trade_type=JSAPI, result_code=SUCCESS, time_end=20190427152056, is_subscribe=N, return_code=SUCCESS}
     */
    @PostMapping("/notify")
    public void  wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        logger.info("[WXPayController.wxNotify] 进入");
        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> map = new HashMap<String, String>();
        map = WXPayUtil.xmlToMap(sb.toString());
        logger.info("微信回调的内容为：{}",map);

        if(!StringUtils.isEmpty(map.get("return_code")) && "SUCCESS".equals(map.get("return_code"))){
            //签名校验
            boolean signFlag = wxPayService.validateSign(map);

            if("SUCCESS".equals(map.get("result_code")) && signFlag){
                String outTradeNo = map.get("out_trade_no");
                Integer totalFee = Integer.valueOf(map.get("total_fee"));
                TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("out_trade_no", outTradeNo));
                Long orderTotalFee = torder.getTotalFee();
                if(orderTotalFee!=null && orderTotalFee.intValue() == totalFee.intValue()){
                    torder.setTransactionId(map.get("transaction_id"));
                    torder.setTimeEnd(map.get("time_end"));
                    torder.setTradeState(WXPayConstants.SUCCESS);
                    taskOrderService.updateById(torder);
                    //返回微信接收到结果
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    Map returnCode = new HashMap();
                    returnCode.put("return_code",WXPayConstants.SUCCESS);
                    out.write(WXPayUtil.mapToXml(returnCode).getBytes());
                    out.flush();
                    out.close();
                }
            }
        }

    }

    //小程序调用接口回查任务订单支付结果
    @PostMapping("/orderQuery")
    @ApiOperation("程序调用接口回查任务订单支付结果")
    public R orderQuery(Long taskId) throws Exception {
        TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if(WXPayConstants.SUCCESS.equals(torder.getTradeState())){
            return R.ok("支付成功");
        }else{
          //从微信平台查询订单支付状态
            String xresdata = wxPayService.orderQueryRequest(torder.getOutTradeNo());
            Map<String, String> map = WXPayUtil.xmlToMap(xresdata);
            String returnCode = map.get("return_code");
            if(!StringUtils.isEmpty(returnCode) && "SUCCESS".equals(returnCode)){
                String resultCode = map.get("result_code");
                if(!StringUtils.isEmpty(resultCode) && "SUCCESS".equals(resultCode)){
                    torder.setTradeState(map.get("trade_state"));
                    taskOrderService.updateById(torder);
                    return R.ok(map.get("trade_state"));
                }
            }
            return R.error();
        }
    }


}
