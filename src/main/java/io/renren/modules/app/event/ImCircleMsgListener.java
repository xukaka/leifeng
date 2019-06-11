package io.renren.modules.app.event;


import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.ImMessageUtils;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.service.ImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_CIRCLE)
public class ImCircleMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImCircleMsgListener.class);

    @Autowired
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle circle message ===>>> " + message);
        JSONObject circleMsg = JSONObject.parseObject(message);
        Long circleId = circleMsg.getLong("circleId");
        Long auditId = circleMsg.getLong("auditId");
        Long fromMemberId = circleMsg.getLong("fromMemberId");
        Long toMemberId = circleMsg.getLong("toMemberId");
        String type = circleMsg.getString("type");

        imService.addCircleNotice(circleId,auditId,fromMemberId,toMemberId,type);
        //设置红点
        imService.setRedDot(toMemberId,3);

    }
}

