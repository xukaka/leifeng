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
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_GROUP)
public class ImMsgListener {
    private static Logger LOG = LoggerFactory.getLogger(ImMsgListener.class);

    @Autowired
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        LOG.info("rabbitMQ handle group message ===>>> " + message);
        JSONObject groupMsg = JSONObject.parseObject(message);
        String from = groupMsg.getString("from");
        String groupId = groupMsg.getString("groupId");
        String businessType = groupMsg.getString("businessType");
        Long businessId = groupMsg.getLong("businessId");
        String content = groupMsg.getString("content");

        imService.addGroupNotice(groupId, from, businessType, businessId, content);
        ImMessageUtils.sendGroupMessage(from, groupId);
    }
}

