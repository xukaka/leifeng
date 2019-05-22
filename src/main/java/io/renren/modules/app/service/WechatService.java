package io.renren.modules.app.service;

import com.alibaba.fastjson.JSON;
import io.renren.modules.app.utils.JsSdkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信服务
 */
@Service
public class WechatService {
    private final static Logger LOGGER = LoggerFactory.getLogger(WechatService.class);

    @Resource
    private  JsSdkUtils jsSdkUtils;

    @Value("${wx.app_id}")
    private String WX_APPID;
    /**
     * 根据jsapi_ticket等参数进行SHA1加密
     * nonceStr 随机字符串
     * timestamp 当前时间戳
     * @param url 当前页面url
     */
    public Map<String,String> createSignature(String url) throws IOException {
        String nonceStr = create_nonce_str();
        String timestamp = create_timestamp();

        String signature = "jsapi_ticket="+ jsSdkUtils.getJsapiTicket();
        signature += "&noncestr="+nonceStr;
        signature += "&timestamp="+timestamp;
        signature += "&url="+url;

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signature.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            LOGGER.error("Signature for SHA-1 is error:{}",e);
        }

        Map<String, String> map = new HashMap<>();
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("signature", signature);
        map.put("appid", WX_APPID);
        return map;
//        return JSON.toJSONString(map, true);
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
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


}

