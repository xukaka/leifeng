package io.renren.modules.app.event;


import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.ImMessageUtils;
import io.renren.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_TASK_STATUS)
public class ImTaskStatusMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImTaskStatusMsgListener.class);

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle task status message ===>>> " + message);
        JSONObject taskStatusMsg = JSONObject.parseObject(message);
        ImMessageUtils.sendSingleMessage(taskStatusMsg);
    }


}

