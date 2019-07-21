package io.renren.modules.app.service;

import com.google.common.collect.Maps;
import io.renren.common.exception.RRException;
import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.config.WXPayConfig;
import io.renren.modules.app.form.WxUserInfoForm;
import io.renren.modules.app.service.impl.TaskServiceImpl;
import io.renren.modules.app.service.impl.WXRequest;
import io.renren.modules.app.utils.HttpClientUtil;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WechatSecurityCheck {
    private final static Logger logger = LoggerFactory.getLogger(WechatSecurityCheck.class);

    private final static String WECHAT_MSG_CHECK_URL = "https://api.weixin.qq.com/wxa/msg_sec_check";
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private WXPayConfig wxPayConfig;
    @Autowired
    private WXRequest wxRequest;

    public void checkMsgSecurity(String content) {
        //获取access_token
        String accessToken = wxRequest.getAccessToken();

        HashMap<String, String> param = Maps.newHashMap();
        param.put("content", content);
        StringEntity msgEntity = new StringEntity(JsonUtil.Java2Json(param), "UTF-8");
        String msgurl = WECHAT_MSG_CHECK_URL + "?access_token=" + accessToken;
        HttpPost msgPost = new HttpPost(msgurl);
        msgPost.setEntity(msgEntity);
        try {

            String secResult = HttpClientUtil.postExecute(msgPost);
            Map secMap = JsonUtil.JsonStr2Java(secResult, Map.class);
            Integer code = (Integer) secMap.get("errcode");
            logger.info("文本安全校验接口结果：{}", secResult);

            if (code != 0) {
                if (code == 87014)
                    throw new RRException("发布的内容敏感字哦，请修改后发布！");
                else
                    throw new RRException("调用文本安全接口报错！");
            }
        } catch (IOException e) {
            logger.error("微信关键字安全接口错误", e);
        }

    }
}
