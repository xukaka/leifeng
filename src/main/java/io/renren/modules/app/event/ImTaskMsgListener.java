package io.renren.modules.app.event;


import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.ImMessageUtils;
import io.renren.common.utils.RabbitMqHelper;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.service.ImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_TASK)
public class ImTaskMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImTaskMsgListener.class);
    @Resource
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle task message ===>>> " + message);
        JSONObject msg = JSONObject.parseObject(message);
        Long fromMemberId = msg.getLong("fromMemberId");
        Long toMemberId = msg.getLong("toMemberId");
        String operate = msg.getString("operate");
        Long taskId = msg.getLong("taskId");
        imService.addTaskNotice(fromMemberId,toMemberId,operate,taskId);

        //设置红点
        imService.setRedDot(toMemberId,1);

    }


}

