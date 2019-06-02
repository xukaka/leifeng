package io.renren.modules.app.event;


import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.ImMessageUtils;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.service.ImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_RED_DOT)
public class ImRedDotMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImRedDotMsgListener.class);

    @Resource
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle red dot status message ===>>> " + message);
        JSONObject msg = JSONObject.parseObject(message);
        long memberId = msg.getLongValue("memberId");
        int redDotType = msg.getIntValue("redDotType");
        imService.setRedDot(memberId,redDotType);
    }


}

