package io.renren.modules.app.event;


import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.ImMessageUtils;
import io.renren.config.RabbitMQConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_NAME)
public class ImMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImMsgListener.class);

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle message ===>>> " + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        String businessCode = jsonObject.getString("businessCode");
        if (StringUtils.equals(businessCode, "0")) {
            String groupId = jsonObject.getString("groupId");
            ImMessageUtils.sendGroupMessage("发布任务", groupId, jsonObject);
        } else if (StringUtils.equals(businessCode, "1")) {
            String to = jsonObject.getString("taskCreatorId");
            ImMessageUtils.sendSingleMessage("领取任务", to, jsonObject);
        } else if (StringUtils.equals(businessCode, "2")) {
            String to = jsonObject.getString("taskReceiverId");
            ImMessageUtils.sendSingleMessage("确认领取任务", to, jsonObject);
        } else if (StringUtils.equals(businessCode, "3")) {
            String to = jsonObject.getString("taskCreatorId");
            ImMessageUtils.sendSingleMessage("开始执行任务", to, jsonObject);
        } else if (StringUtils.equals(businessCode, "4")) {
            String to = jsonObject.getString("taskCreatorId");
            ImMessageUtils.sendSingleMessage("提交任务", to, jsonObject);
        } else if (StringUtils.equals(businessCode, "5")) {
            String to = jsonObject.getString("taskReceiverId");
            ImMessageUtils.sendSingleMessage("确认任务完成", to, jsonObject);
        }

    }
}

