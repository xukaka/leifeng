package io.renren.modules.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RRException;
import io.renren.common.utils.HttpHelper;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.dto.WXSession;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class WXRequest {

    private final static Logger logger = LoggerFactory.getLogger(WXRequest.class);

    @Value("${wx.app_secret}")//小程序
    public String appSecret="";

    @Value("${wx.app_id}")//小程序
    public String appId="";

    @Value("${wx.code_session_url}")
    public String wxLoginUrl = "";

    @Autowired
    private RedisUtils redisUtils;


    public WXSession loginWXWithCode(String code){
        //创建httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        WXSession wxSession = null;

        try {
           //说明见微信开发文档https://developers.weixin.qq.com/miniprogram/dev/api/code2Session.html
           URIBuilder builder = new URIBuilder(wxLoginUrl);
           builder.addParameter("appid",appId);
           builder.addParameter("secret",appSecret);
           builder.addParameter("js_code",code);
           builder.addParameter("grant_type","authorization_code");

           HttpGet httpGet = new HttpGet(builder.build());
           response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                logger.info("微信登录请求url的结果为："+resultString);
                if(!StringUtils.isEmpty(resultString)){
                    JSONObject wxobject = JSONObject.parseObject(resultString);
                    if(null==wxobject.getInteger("errcode")){  //请求成功
                        wxSession = new WXSession();
                        wxSession.setOpenid(wxobject.getString("openid"));
                        wxSession.setSessionKey(wxobject.getString("session_key"));
                    }else{
                        throw new RRException(wxobject.getString("errmsg"));
                    }
                }
            }

        }catch (Exception e){
            logger.error("code2session请求错误:",e);
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
        return wxSession;
    }

    /**
     * 微信access_token
     */
    public  String getAccessToken(){

        try {
            String value = redisUtils.get("WEIXIN_BASE_ACCESS_TOKEN");
            if (!org.apache.commons.lang3.StringUtils.isEmpty(value)) {
                logger.info("Get base access_token from redis is successful.value:{}",value);
                return value;
            }else{
                synchronized (this) {
                    //缓存中没有、或已经失效
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+ appSecret;
                    String rs = HttpHelper.get(url,3000);

                    JSONObject obj_content = JSONObject.parseObject(rs);
                    String accessToken = obj_content.getString("access_token");
                    int time = Integer.parseInt(obj_content.getString("expires_in"));

                    //写缓存
                    redisUtils.set("WEIXIN_BASE_ACCESS_TOKEN", accessToken, time - 3600);
                    logger.info("Set base access_token to redis is successful.parameters time:{},realtime",time,time-3600);
                    return accessToken;
                }
            }
        } catch (Exception e) {
            logger.error("Get base access_token from redis is error.");
        }
        return null;
    }


}
