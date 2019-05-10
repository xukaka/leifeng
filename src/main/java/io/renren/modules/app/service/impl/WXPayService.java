package io.renren.modules.app.service.impl;

import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.OrderNoUtil;
import io.renren.modules.app.config.WXPayConfig;
import io.renren.modules.app.utils.ReqUtils;
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
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class WXPayService {
    private final static Logger logger = LoggerFactory.getLogger(WXPayService.class);
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
        }else{
            throw new Exception(map.get("return_msg"));
        }
        System.out.println("微信再签名后的包装参数为="+ JsonUtil.Java2Json(backData));
        return backData;
    }

    public boolean validateSign(Map<String,String> map){
        try {
            String vsign = WXPayUtil.generateSignature(map, config.getKey());
            String sign = map.get("sign");
            if(!vsign.equals(sign)){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("签名校验出错异常");
            return false;
        }
        return true;
    }

    public String orderQueryRequest(String outTradeNo) throws Exception{
        //检验参数必填
        checkWXPayConfig();

        Map<String,String> reqdata = new HashMap<>();
        reqdata.put("appid",config.getAppId());
        reqdata.put("mch_id",config.getMchId());
        reqdata.put("out_trade_no",outTradeNo);
        reqdata.put("nonce_str",WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign",sign);
        String data = WXPayUtil.mapToXml(reqdata);

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

        HttpPost httpPost = new HttpPost(this.config.getOrderQuery());

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

    public String transferMoneyRequest(String openId,String realName,String amount) throws Exception {
        //检验参数必填
        checkWXPayConfig();

        Map<String,String> reqdata = new HashMap<>();
        reqdata.put("mch_appid",config.getAppId());
        reqdata.put("mchid",config.getMchId());
        reqdata.put("nonce_str",WXPayUtil.generateNonceStr());
        reqdata.put("partner_trade_no", OrderNoUtil.generateOrderNo(Long.valueOf(new Random().nextInt(100000))));
        reqdata.put("openid",openId);
        reqdata.put("check_name","FORCE_CHECK");//NO_CHECK：不校验真实姓名,FORCE_CHECK：强校验真实姓名
        reqdata.put("re_user_name",realName);
        reqdata.put("amount",amount);
        reqdata.put("desc","企业转账提现");
        reqdata.put("spbill_create_ip", ReqUtils.getRequest().getRemoteAddr());

        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign",sign);
        String data = WXPayUtil.mapToXml(reqdata);

        char[] password = config.getMchId().toCharArray();
        InputStream certStream = config.getCertStream();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(certStream, password);

        // 实例化密钥库 & 初始化密钥工厂
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password);

        // 创建 SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                new DefaultHostnameVerifier());

        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslConnectionSocketFactory)
                        .build(),
                null,
                null,
                null
        );

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        HttpPost httpPost = new HttpPost(this.config.getTransferUrl());

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

    public String refundRequest(String transactionId, Long taksId, String amount) throws Exception {
        //检验参数必填
        checkWXPayConfig();

        Map<String,String> reqdata = new HashMap<>();
        reqdata.put("appid",config.getAppId());
        reqdata.put("mch_id",config.getMchId());
        reqdata.put("nonce_str",WXPayUtil.generateNonceStr());
        reqdata.put("out_refund_no", "RF"+OrderNoUtil.generateOrderNo(taksId));
        reqdata.put("transaction_id",transactionId);
        reqdata.put("total_fee",amount);
        reqdata.put("refund_fee",amount);
        reqdata.put("refund_desc","退款");

        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign",sign);
        String data = WXPayUtil.mapToXml(reqdata);

        char[] password = config.getMchId().toCharArray();
        InputStream certStream = config.getCertStream();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(certStream, password);

        // 实例化密钥库 & 初始化密钥工厂
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password);

        // 创建 SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                new DefaultHostnameVerifier());

        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslConnectionSocketFactory)
                        .build(),
                null,
                null,
                null
        );

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        HttpPost httpPost = new HttpPost(this.config.getRefund());

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8*1000).setConnectTimeout(6*1000).build();
        httpPost.setConfig(requestConfig);
        logger.info("微信退款平台请求参数为："+data);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }
}
