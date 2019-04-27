package io.renren.modules.app.controller.pay;

import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.OrderNoUtil;
import io.renren.modules.app.service.impl.WXPayService;
import io.renren.modules.app.utils.ReqUtils;
import io.renren.modules.app.utils.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping("/app/wx/pay")
@Api(tags = "微信支付")
public class WXPayController {
    private final static Logger logger = LoggerFactory.getLogger(WXPayController.class);
    @Autowired
    WXPayService wxPayService;

    @PostMapping("/prePay")
    @ApiOperation("微信预下订单接口")
    public Map<String, String> prePay(String prodDesc, float totalFee, String openid) throws Exception {
        logger.info("[WXPayController.prePay] request:prodDesc={},totleFee={},openid={}",prodDesc,totalFee,openid);
        Map<String,String> reqData = new HashMap<>();
        reqData.put("body",prodDesc); //商品描述
        reqData.put("out_trade_no", "NO"+OrderNoUtil.generateOrderNo());
        reqData.put("total_fee",String.valueOf((int)(totalFee*100))); //交易的金额，单位为分
        reqData.put("spbill_create_ip", ReqUtils.getRequest().getRemoteAddr());
        reqData.put("openid",openid);
        Map<String, String> reqDataComp = wxPayService.fillRequestData(reqData);
        logger.info("微信预下订单请求参数：{}", JsonUtil.Java2Json(reqDataComp));
        String wxResponse = wxPayService.prePayRequest(WXPayUtil.mapToXml(reqDataComp));
        logger.info("微信预下订单请求结果：{}",wxResponse);

        Map<String, String> wxPayMap = wxPayService.reGenerateParamForApp(wxResponse);
        return wxPayMap;
    }

    @PostMapping("/notify")
    public void  wxNotify(HttpServletRequest request) throws Exception{
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
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = map.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

    }
}
