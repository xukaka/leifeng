/*package io.renren.common.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMqHelper {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String queueName,String messageJson){
        rabbitTemplate.convertAndSend(queueName,messageJson);
    }
}*/
