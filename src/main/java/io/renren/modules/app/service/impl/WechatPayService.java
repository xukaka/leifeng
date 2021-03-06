package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.OrderNoUtil;
import io.renren.common.utils.RedisKeys;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.config.WXPayConfig;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.entity.task.WithdrawalOrderEntity;
import io.renren.modules.app.service.MemberWalletLogService;
import io.renren.modules.app.service.MemberWalletService;
import io.renren.modules.app.service.WithdrawalOrderService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class WechatPayService {
    private final static Logger logger = LoggerFactory.getLogger(WechatPayService.class);
    @Autowired
    private MemberWalletService memberWalletService;
    @Autowired
    private MemberWalletLogService memberWalletLogService;

    @Autowired
    private WithdrawalOrderService withdrawalOrderService;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WXPayConfig config;

    private SignType signType = SignType.MD5;

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
        if (this.config.getWXPayDomain() == null) {
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
        reqData.put("trade_type", config.getTradeType());
        reqData.put("notify_url", config.getNotifyUrl());
        //用微信指定的签名算法生成sign
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), WXPayConstants.SignType.MD5));
        return reqData;
    }

    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(String prodDesc, String outTradeNo, String totalFee, String spbillCreateIp, String openid) throws Exception {
        Map<String, String> reqData = new HashMap<>();
        reqData.put("body", prodDesc); //商品描述
        reqData.put("out_trade_no", outTradeNo);
        reqData.put("total_fee", totalFee); //交易的金额，单位为分
        reqData.put("spbill_create_ip", spbillCreateIp);
        reqData.put("openid", openid);

        reqData.put("appid", config.getAppId());
        reqData.put("mch_id", config.getMchId());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        reqData.put("sign_type", WXPayConstants.MD5);
        reqData.put("trade_type", config.getTradeType());
        reqData.put("notify_url", config.getNotifyUrl());
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
        } else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            } else if (WXPayConstants.MD5.equals(signTypeInData)) {
                signType = SignType.MD5;
            } else if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            } else {
                throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
            }
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }


    public String prePayRequest(String data) throws Exception {
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

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8 * 1000).setConnectTimeout(6 * 1000).build();
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
    public Map<String, String> reGenerateParamForApp(String wxResponseData) throws Exception {
        Map<String, String> map = WXPayUtil.xmlToMap(wxResponseData);
        Map<String, String> backData = new HashMap<>();
        if ("SUCCESS".equals(map.get("return_code"))) {
            if ("SUCCESS".equals(map.get("result_code"))) {
                String prePayId = map.get("prepay_id");
                backData.put("package", "prepay_id=" + prePayId);
                backData.put("nonceStr", WXPayUtil.generateNonceStr());
                backData.put("signType", "MD5");
                backData.put("appId", map.get("appid"));
                backData.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                backData.put("paySign", WXPayUtil.generateSignature(backData, config.getKey(), WXPayConstants.SignType.MD5));
            }
        } else {
            throw new Exception(map.get("return_msg"));
        }
        return backData;
    }

    public boolean validateSign(Map<String, String> map) {
        try {
            String vsign = WXPayUtil.generateSignature(map, config.getKey());
            String sign = map.get("sign");
            if (!vsign.equals(sign)) {
                return false;
            }
        } catch (Exception e) {
            logger.info("签名校验出错异常", e);
            return false;
        }
        return true;
    }

    public String orderQueryRequest(String outTradeNo) throws Exception {
        //检验参数必填
        checkWXPayConfig();

        Map<String, String> reqdata = new HashMap<>();
        reqdata.put("appid", config.getAppId());
        reqdata.put("mch_id", config.getMchId());
        reqdata.put("out_trade_no", outTradeNo);
        reqdata.put("nonce_str", WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign", sign);
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

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8 * 1000).setConnectTimeout(6 * 1000).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }


    public String refundQueryRequest(String outTradeNo) throws Exception {
        //检验参数必填
        checkWXPayConfig();

        Map<String, String> reqdata = new HashMap<>();
        reqdata.put("appid", config.getAppId());
        reqdata.put("mch_id", config.getMchId());
        reqdata.put("out_trade_no", outTradeNo);
        reqdata.put("nonce_str", WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign", sign);
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

        HttpPost httpPost = new HttpPost(this.config.getRefundQuery());

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8 * 1000).setConnectTimeout(6 * 1000).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }

    public String transferMoneyRequest(String openId, String realName, String amount) throws Exception {
        //检验参数必填
        checkWXPayConfig();

        Map<String, String> reqdata = new HashMap<>();
        reqdata.put("mch_appid", config.getAppId());
        reqdata.put("mchid", config.getMchId());
        reqdata.put("nonce_str", WXPayUtil.generateNonceStr());
        reqdata.put("partner_trade_no", OrderNoUtil.generateOrderNo((long) new Random().nextInt(100000)));
        reqdata.put("openid", openId);
        reqdata.put("check_name", "NO_CHECK");//NO_CHECK：不校验真实姓名,FORCE_CHECK：强校验真实姓名
//        reqdata.put("re_user_name", realName);
        reqdata.put("amount", amount);
        reqdata.put("desc", "企业转账提现");
        reqdata.put("spbill_create_ip", ReqUtils.getRemoteAddr());

        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign", sign);
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

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8 * 1000).setConnectTimeout(6 * 1000).build();
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

        Map<String, String> reqdata = new HashMap<>();
        reqdata.put("appid", config.getAppId());
        reqdata.put("mch_id", config.getMchId());
        reqdata.put("nonce_str", WXPayUtil.generateNonceStr());
        reqdata.put("out_refund_no", "RF" + OrderNoUtil.generateOrderNo(taksId));
        reqdata.put("transaction_id", transactionId);
        reqdata.put("total_fee", amount);
        reqdata.put("refund_fee", amount);
        reqdata.put("refund_desc", "退款");

        String sign = WXPayUtil.generateSignature(reqdata, config.getKey());
        reqdata.put("sign", sign);
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

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8 * 1000).setConnectTimeout(6 * 1000).build();
        httpPost.setConfig(requestConfig);
        logger.info("微信退款平台请求参数为：" + data);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }


    /**
     * 提现申请
     *
     * @param memberId
     * @param amount
     */
    @Transactional
    public void preWithdrawal(Long memberId, Long amount) {
        MemberWalletEntity wallet = memberWalletService.selectOne(new EntityWrapper<MemberWalletEntity>()
                .eq("member_id", memberId));
        checkPreWithdrawal(memberId, amount, wallet);
        //生成提现订单
        String outTradeNo = "TX" + OrderNoUtil.generateOrderNo(memberId);
        WithdrawalOrderEntity order = new WithdrawalOrderEntity();
        order.setTotalFee(amount);
        order.setTradeState("AUDIT");//审核状态
        order.setAttach("提现");
        order.setMemberId(memberId);
        order.setOutTradeNo(outTradeNo);
        order.setCreateTime(DateUtils.now());
        withdrawalOrderService.insert(order);

        //用户钱包金额减少
        memberWalletService.incMoney(memberId, -amount);

        //记录钱包金额变动日志
        MemberWalletLogEntity log = new MemberWalletLogEntity();
        log.setMemberId(memberId);
        log.setChangeMoney(-amount);
        log.setMoney(wallet.getMoney() - amount);
        log.setOutTradeNo(outTradeNo);
        log.setType("withdrawal");
        log.setRemark("提现");
        log.setCreateTime(DateUtils.now());
        memberWalletLogService.insert(log);
    }

    /**
     * 校验预提现
     * @param memberId
     * @param amount
     * @param wallet
     */
    private void checkPreWithdrawal(Long memberId, Long amount, MemberWalletEntity wallet) {
        if (wallet == null || wallet.getMoney() < amount) {
            throw new RRException("钱包提现余额不足");
        }
        if (amount < 2000){
            throw new RRException("单次提现金额不低于20元");
        }
        if (amount > 100000){
            throw new RRException("单次提现金额不高于1000元");
        }
        long todayTotalWithdrawalCount = redisUtils.incr(RedisKeys.PRE_WITHDRAWAL_COUNT_LIMIT+memberId, DateUtils.secondsLeftToday(),1);
        if (todayTotalWithdrawalCount> 10){
            throw new RRException("今日提现次数已达上限");
        }
    }

    /**
     * 提现
     */
    public Map<String, String> withdrawal(String outTradeNo) throws Exception {
        Map<String, String> result = new HashMap<>();
        WithdrawalOrderEntity order = withdrawalOrderService.selectOne(new EntityWrapper<WithdrawalOrderEntity>()
                .eq("out_trade_no", outTradeNo));
        if (order == null) {
            throw new RRException("提现订单为空");
        }
        if (!WXPayConstants.AUDIT.equals(order.getTradeState())) {
            throw new RRException("订单已经处理过了，不能重复提现,TradeState=" + order.getTradeState());
        }
        MemberWalletEntity wallet = memberWalletService.selectOne(new EntityWrapper<MemberWalletEntity>()
                .eq("member_id", order.getMemberId()));

        String transData = this.transferMoneyRequest(wallet.getOpenId(), wallet.getRealName(), calRealTotalFee(order));
        logger.info("转账提现接口微信返回结果：{}", transData);
        Map<String, String> map = WXPayUtil.xmlToMap(transData);
        if (WXPayConstants.SUCCESS.equals(map.get("return_code"))) {
            if (WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
                order.setTradeState(WXPayConstants.SUCCESS);
                withdrawalOrderService.updateById(order);

                result.put("tradeNo", map.get("partner_trade_no"));
                result.put("paymentNo", map.get("payment_no"));
                result.put("paymentTime", map.get("payment_time"));
            } else {
                logger.info(map.get("err_code") + ":" + map.get("err_code_des"));
                result.put("error", map.get("err_code") + ":" + map.get("err_code_des"));
            }
        } else {
            logger.info(map.get("return_msg"));
            result.put("return_msg", map.get("return_msg"));
        }
        return result;
    }

    /**
     * 计算实际的提现金额
     *
     * @param order
     * @return
     */
    private String calRealTotalFee(WithdrawalOrderEntity order) {
        /**
         * 微信手续费0.6% + 平台服务费0.2% = 0.8%
         * 每次提现不少于50元=5000分
         */
        BigDecimal orderTotalFee = new BigDecimal(order.getTotalFee());
        BigDecimal withdrawalRate = new BigDecimal(0.992);
        BigDecimal realTotalFee = orderTotalFee.multiply(withdrawalRate);
        return String.valueOf(realTotalFee.longValue());

    }


    /**
     * 人工 提现（临时方案，后期需调整为自动提现：参考WechatPayService.withdrawal方法）
     */
    public Map<String, String> manualWithdrawal(String outTradeNo) throws Exception {
        Map<String, String> result = new HashMap<>();
        WithdrawalOrderEntity order = withdrawalOrderService.selectOne(new EntityWrapper<WithdrawalOrderEntity>()
                .eq("out_trade_no", outTradeNo));
        if (order == null) {
            throw new RRException("提现订单为空");
        }
        if (!WXPayConstants.AUDIT.equals(order.getTradeState())) {
            throw new RRException("订单已经处理过了，不能重复提现,TradeState=" + order.getTradeState());
        }

        order.setTradeState(WXPayConstants.SUCCESS);
        withdrawalOrderService.updateById(order);

        result.put("status", "success");
        result.put("message", "人工提现成功，请及时付款到用户账号");

        return result;
    }
}
