package io.renren.modules.app.service.impl;

import io.renren.common.utils.JsonUtil;
import io.renren.modules.app.config.WXPayConfig;
import io.renren.modules.app.utils.WXPayConstants;
import io.renren.modules.app.utils.WXPayConstants.SignType;
import io.renren.modules.app.utils.WXPayUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class WXPayService {

    @Autowired
    private WXPayConfig config;

    private SignType signType= SignType.MD5;

    private void checkWXPayConfig() throws Exception {
        if (this.config == null) {
            throw new Exception("config is null");
        }
        if (this.config.getAppId() == null || this.config.getAppId().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getMchId() == null || this.config.getMchId().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getWXPayDomain() == null){
            throw new Exception("config.getWXPayDomain() is null");
        }

        if (this.config.getHttpConnectTimeoutMs() < 10) {
            throw new Exception("http connect timeout is too small");
        }
        if (this.config.getHttpReadTimeoutMs() < 10) {
            throw new Exception("http read timeout is too small");
        }

    }

    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", config.getAppId());
        reqData.put("mch_id", config.getMchId());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        reqData.put("sign_type", WXPayConstants.MD5);
        reqData.put("trade_type",config.getTradeType());
        reqData.put("notify_url",config.getNotifyUrl());
        //用微信指定的签名算法生成sign
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), WXPayConstants.SignType.MD5));
        return reqData;
    }

    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
    }

    /**
     * 判断支付结果通知中的sign是否有效
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        String signTypeInData = reqData.get(WXPayConstants.FIELD_SIGN_TYPE);
        SignType signType;
        if (signTypeInData == null) {
            signType = SignType.MD5;
        }
        else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.MD5.equals(signTypeInData)) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            }
            else {
                throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
            }
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }


    public String prePayRequest(String data) throws Exception{
        //检验参数必填
        checkWXPayConfig();
        BasicHttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null,
                null,
                null
        );
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        HttpPost httpPost = new HttpPost(this.config.getWXPayDomain());

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8*1000).setConnectTimeout(6*1000).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }

    //根据预下订单接口的返回数据重新签名返回参数给移动端发起支付
    public Map<String,String> reGenerateParamForApp(String wxResponseData) throws Exception {
        Map<String, String> map = WXPayUtil.xmlToMap(wxResponseData);
        System.out.println("xml转成map后为="+JsonUtil.Java2Json(map));
        Map<String,String> backData = new HashMap<>();
        String returnCode = map.get("return_code");
        if(!StringUtils.isEmpty(returnCode) && "SUCCESS".equals(returnCode)){
            String resultCode = map.get("result_code");
            if(!StringUtils.isEmpty(resultCode) && "SUCCESS".equals(resultCode)){
                String prePayId = map.get("prepay_id");
                backData.put("package","prepay_id="+prePayId);
                backData.put("nonceStr",WXPayUtil.generateNonceStr());
                backData.put("signType","MD5");
                backData.put("appId",map.get("appid"));
                backData.put("timeStamp",String.valueOf(System.currentTimeMillis()/1000));
                backData.put("paySign",WXPayUtil.generateSignature(backData, config.getKey(), WXPayConstants.SignType.MD5));
            }
        }
        System.out.println("微信再签名后的包装参数为="+ JsonUtil.Java2Json(backData));
        return backData;
    }


}
