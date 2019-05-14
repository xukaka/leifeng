package io.renren.modules.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Component
@ConfigurationProperties("wx")
public class WXPayConfig {

    //小程序appid
    private String appId;

    //商户号id
    private String mchId;

    //支付类型
    private String tradeType;

    private String key;

    private String WXPayDomain;

    private String notifyUrl;

    private String orderQuery;

    private String transferUrl;

    private String cert;

    private String refund;

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容

    abstract InputStream getCertStream();*/

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpConnectTimeoutMs() {
        return 6*1000;
    }

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpReadTimeoutMs() {
        return 8*1000;
    }

    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     * @return
     */
    //abstract IWXPayDomain getWXPayDomain();

    /**
     * 是否自动上报。
     * 若要关闭自动上报，子类中实现该函数返回 false 即可。
     *
     * @return
     */
    public boolean shouldAutoReport() {
        return true;
    }

    /**
     * 进行健康上报的线程的数量
     *
     * @return
     */
    public int getReportWorkerNum() {
        return 6;
    }


    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     *
     * @return
     */
    public int getReportQueueMaxSize() {
        return 10000;
    }

    /**
     * 批量上报，一次最多上报多个数据
     *
     * @return
     */
    public int getReportBatchSize() {
        return 10;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWXPayDomain() {
        return WXPayDomain;
    }

    public void setWXPayDomain(String WXPayDomain) {
        this.WXPayDomain = WXPayDomain;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOrderQuery() {
        return orderQuery;
    }

    public void setOrderQuery(String orderQuery) {
        this.orderQuery = orderQuery;
    }

    public String getTransferUrl() {
        return transferUrl;
    }

    public void setTransferUrl(String transferUrl) {
        this.transferUrl = transferUrl;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public InputStream getCertStream(){
        try {
            ClassPathResource source = new ClassPathResource(getCert());
            return source.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
