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
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_TASK_STATUS)
public class ImTaskStatusMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImTaskStatusMsgListener.class);

    @Resource
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle task status message ===>>> " + message);
        JSONObject msg = JSONObject.parseObject(message);
        String from = msg.getString("from");
        String to = msg.getString("to");
        String content = msg.getString("content");
        Long taskId = msg.getLong("taskId");

        imService.addTaskStatusNotice(from,to,content,taskId);
        ImMessageUtils.sendSingleMessage(from, content, to, null);
    }


}

