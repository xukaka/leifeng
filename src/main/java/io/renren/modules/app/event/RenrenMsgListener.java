package io.renren.modules.app.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
//@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
public class RenrenMsgListener {
    private static Logger logger = LoggerFactory.getLogger(RenrenMsgListener.class);

    //    @RabbitHandler
    public void handleMessage(String message) {
        logger.info(message);
//        JSONObject jsonObject = JSONObject.parseObject(message);

    }
}

