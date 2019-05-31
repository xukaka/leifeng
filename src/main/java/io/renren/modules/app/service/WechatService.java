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
//        String myUrl = "https://pet.fangzheng.fun/";
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



    //微信网页授权
    private Map<String,String> getOpenIdAndAccessToken(String code){
        Map<String,String> map = new HashMap<>();

        //创建httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            URIBuilder builder = new URIBuilder(aouth2Url);
            builder.addParameter("appid",appId);
            builder.addParameter("secret",appSecret);
            builder.addParameter("code",code);
            builder.addParameter("grant_type","authorization_code");

            HttpGet httpGet = new HttpGet(builder.build());
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                LOGGER.info("微信网页授权请求url的结果为："+resultString);
                if(!StringUtils.isEmpty(resultString)){
                    JSONObject wxobject = JSONObject.parseObject(resultString);
                    String openid = wxobject.getString("openid");
                    if(!StringUtils.isEmpty(openid)){  //请求成功
                        map.put("openId",wxobject.getString("openid"));
                        map.put("accessToken",wxobject.getString("access_token"));
                    }
                }
            }

        }catch (Exception e){
            LOGGER.error("getOpen请求错误:",e);
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    //返回微信公众号的unionid
    public String getUnionid(String code){

       Map<String,String> map =  getOpenIdAndAccessToken(code);

        //创建httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            URIBuilder builder = new URIBuilder(userinfoUrl);
            LOGGER.info("getUnionid map={}",map);
            builder.addParameter("openid",map.get("openid"));
            builder.addParameter("access_token",map.get("accessToken"));
            builder.addParameter("lang","zh_CN" );

            HttpGet httpGet = new HttpGet(builder.build());
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                LOGGER.info("微信公众号授权请求url的结果为："+resultString);
                if(!StringUtils.isEmpty(resultString)){
                    JSONObject wxobject = JSONObject.parseObject(resultString);
                    String unionid = wxobject.getString("unionid");
                    if(!StringUtils.isEmpty(unionid)){  //请求成功
                       return unionid;
                    }
                }
            }

        }catch (Exception e){
            LOGGER.error("getOpen请求错误:",e);
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}

