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
@RabbitListener(queues = RabbitMQConfig.IM_QUEUE_NAME)
public class ImMsgListener {
    private static Logger logger = LoggerFactory.getLogger(ImMsgListener.class);

    @Autowired
    private ImService imService;

    @RabbitHandler
    public void handleMessage(String message) {
        logger.info("rabbitMQ handle message ===>>> " + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        String businessCode = jsonObject.getString("businessCode");
        String to = null;
        switch (businessCode) {
            case "0":
                String groupId = jsonObject.getString("groupId");
                imService.addGroupNotice(Long.parseLong(groupId),jsonObject.toJSONString());
                ImMessageUtils.sendGroupMessage("发布任务", groupId, jsonObject);
                break;
            case "1":
                to = jsonObject.getString("taskCreatorId");
                ImMessageUtils.sendSingleMessage("领取任务", to, jsonObject);
                break;
            case "2":
                to = jsonObject.getString("taskReceiverId");
                ImMessageUtils.sendSingleMessage("确认领取任务", to, jsonObject);
                break;
            case "3":
                to = jsonObject.getString("taskCreatorId");
                ImMessageUtils.sendSingleMessage("开始执行任务", to, jsonObject);
                break;
            case "4":
                to = jsonObject.getString("taskCreatorId");
                ImMessageUtils.sendSingleMessage("提交任务", to, jsonObject);
                break;
            case "5":
                to = jsonObject.getString("taskReceiverId");
                ImMessageUtils.sendSingleMessage("确认任务完成", to, jsonObject);
                break;
            case "6":
                to = jsonObject.getString("taskCreatorId");
                ImMessageUtils.sendSingleMessage("领取人取消任务", to, jsonObject);
                break;
            case "7":
                to = jsonObject.getString("taskReceiverId");
                ImMessageUtils.sendSingleMessage("发布人取消任务", to, jsonObject);
                break;
        }

    }

    /*public static void main(String[] args){

            JSONObject extras = new JSONObject();
            extras.put("businessCode", "0");//0，发布任务
            extras.put("taskId", i);
            extras.put("taskTitle","任务消息测试33333");
            extras.put("taskCreatorId", 27);
            extras.put("taskCreatorAvatar", "https://wx.qlogo.cn/mmopen/vi_32/6W4l7KQrJBDyjxtiaZRJCmnEenmf5V0UrjsWHsufgIeeLZQGmXHyRN6ib1UyfXNNibMFrWpCVM5WedqwjZor0xp4A/132");
            extras.put("taskCreatorNickName", "江舟");
            extras.put("groupId", 100);
            logger.info("推消息到关注我的组，extras=" + extras.toJSONString());

            JSONObject jsonObject = JSONObject.parseObject(extras.toJSONString());
            String businessCode = jsonObject.getString("businessCode");
            if (StringUtils.equals(businessCode, "0")) {
                String groupId = jsonObject.getString("groupId");
                ImMessageUtils.sendGroupMessage("发布任务", groupId, jsonObject);
            }


        logger.info("over......");

    }*/

    public static void main(String[] args){
        Long groupId  = 27L;
        JSONObject extras = new JSONObject();
        extras.put("businessCode", "0");//0，发布任务
        extras.put("taskId", 47);
        extras.put("taskTitle", "测试任务");
        extras.put("taskCreatorId", 27);
        extras.put("taskCreatorAvatar", "www.baidu.com");
        extras.put("taskCreatorNickName", "江舟");
        extras.put("groupId", groupId);
        logger.info("推消息到关注我的组，extras=" + extras.toJSONString());
//        imService.addGroupNotice(groupId,extras.toJSONString());
        ImMessageUtils.sendGroupMessage("发布任务", String.valueOf(groupId), extras);


    }
}

