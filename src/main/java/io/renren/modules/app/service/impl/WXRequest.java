package io.renren.modules.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RRException;
import io.renren.modules.app.dto.WXSession;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class WXRequest {

    private final static Logger logger = LoggerFactory.getLogger(WXRequest.class);

    @Value("${wx.app_secret}")
    public String appSecret="";

    @Value("${wx.app_id}")
    public String appId="";

    @Value("${wx.code_session_url}")
    public String wxLoginUrl = "";

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
}
