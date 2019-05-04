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
    private static Logger logger = LoggerFactory.getLogger(ImMsgListener.class);

    @Autowired
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle group message ===>>> " + message);
        JSONObject groupMsg = JSONObject.parseObject(message);
        String from = groupMsg.getString("from");
        String groupId = groupMsg.getString("groupId");
        String businessType = groupMsg.getString("businessType");
        Long businessId = groupMsg.getLong("businessId");
        String content = groupMsg.getString("content");

        imService.addGroupNotice(groupId, from, businessType, businessId, content);
        ImMessageUtils.sendGroupMessage(from, groupId);

    }

    public static void main(String[] args) {
        Long groupId = 27L;
        JSONObject extras = new JSONObject();
        extras.put("businessCode", "18");//0，发布任务
        extras.put("taskId", 47);
        extras.put("taskTitle", "测试任务");
        extras.put("taskCreatorId", 27);
        extras.put("taskCreatorAvatar", "www.baidu.com");
        extras.put("taskCreatorNickName", "江舟");
        logger.info("推消息到关注我的组，extras=" + extras.toJSONString());
//        imService.addGroupNotice(groupId,extras.toJSONString());
        ImMessageUtils.sendSingleMessage("AdminTaskMsg", "尊敬的雷锋您好，您发送的任务状态有更新，请点击查看详情", String.valueOf(20), extras);


    }
}

