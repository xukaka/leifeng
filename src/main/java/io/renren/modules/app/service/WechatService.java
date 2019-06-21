package io.renren.modules.app.service;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.utils.JsSdkUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信公众号服务
 */
@Service
public class WechatService {
    private final static Logger LOGGER = LoggerFactory.getLogger(WechatService.class);

    @Resource
    private  JsSdkUtils jsSdkUtils;

    @Value("${wechat.official.account.app_id}")
    private String appId;//公众号appid

    @Value("${wechat.official.account.app_secret}")
    private String appSecret;

    @Value("${wechat.official.account.aouth2_url}")
    public String aouth2Url;

    @Value("${wechat.official.account.userinfo_url}")
    public String userinfoUrl;

    /**
     * 根据jsapi_ticket等参数进行SHA1加密
     * nonceStr 随机字符串
     * timestamp 当前时间戳
     * @param url 当前页面url
     */
    public Map<String,Object> createSignature(String url) {
        String nonceStr = create_nonce_str();
        long timestamp = create_timestamp();

        StringBuilder sb  = new StringBuilder();
        sb.append("jsapi_ticket=") .append(jsSdkUtils.getJsapiTicket())
                .append("&noncestr=").append(nonceStr)
                .append("&timestamp=").append(timestamp)
                .append("&url=").append(url);
        String signature = null;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sb.toString().getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());

        } catch (Exception e) {
            LOGGER.error("Signature for SHA-1 is error:{}",e);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("signature", signature);
        map.put("appid", appId);
        return map;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    private static long create_timestamp() {
        return System.currentTimeMillis() / 1000;
    }




}

