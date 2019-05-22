package io.renren.modules.app.utils;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RRException;
import io.renren.common.utils.HttpHelper;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.service.impl.DiaryServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@Component
public class JsSdkUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(JsSdkUtils.class);

    @Resource
    private RedisUtils redisUtils;
    @Value("${wx.app_id}")
    private String WX_APPID;

    @Value("${wx.app_secret}")
    private String WX_APPSECRET;
    /**
     * 微信全局票据 ---->>>> access_token
     */
    public  String getBaseAccessToken(){

        try {
            String value = redisUtils.get("WEIXIN_BASE_ACCESS_TOKEN");
            if (!StringUtils.isEmpty(value)) {
                LOGGER.info("Get base access_token from redis is successful.value:{}",value);
                return value;
            }else{
                synchronized (this) {
                    //缓存中没有、或已经失效
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WX_APPID+"&secret="+ WX_APPSECRET;
                    String rs = HttpHelper.get(url,3000);

                    JSONObject obj_content = JSONObject.parseObject(rs);
                    String accessToken = obj_content.getString("access_token");
                    Integer time = Integer.parseInt(obj_content.getString("expires_in").toString());

                    //写缓存
                    redisUtils.set("WEIXIN_BASE_ACCESS_TOKEN", accessToken, time - 3600);
                    LOGGER.info("Set base access_token to redis is successful.parameters time:{},realtime",time,time-3600);
                    return accessToken;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Get base access_token from redis is error.");
        }
        return null;
    }

    /**
     * jsapi_ticket是公众号用于调用微信JS接口的临时票据
     */
    public  String getJsapiTicket(){
        try {
            String value = redisUtils.get("WEIXIN_JS_API_TICKET");
            if (!StringUtils.isEmpty(value)) {
                return value;
            }else{
                synchronized (this) {
                    //缓存中没有、或已经失效
                    //获取全局的access_token，唯一票据
                    String accessToken = getBaseAccessToken();

                    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";

                    String rs = HttpHelper.get(url,3000);

                    JSONObject obj_content = JSONObject.parseObject(rs);
                    String jsapi_ticket = obj_content.getString("ticket");
                    Integer time = Integer.parseInt(obj_content.getString("expires_in").toString());

                    //写缓存
                    redisUtils.set("WEIXIN_JS_API_TICKET", jsapi_ticket, time - 3600);

                    return jsapi_ticket;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Get js_api_ticket from redis is error:{}",e);
        }

        return null;
    }




}
