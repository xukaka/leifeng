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

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle circle message ===>>> " + message);
        JSONObject circleMsg = JSONObject.parseObject(message);
        ImMessageUtils.sendSingleMessage(circleMsg);
    }
}

