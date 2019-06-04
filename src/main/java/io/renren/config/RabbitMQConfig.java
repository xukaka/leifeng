
package io.renren.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public final static String QUEUE_NAME = "queue-name-online";
    public final static String IM_QUEUE_RED_DOT = "queue-name-reddot";
    public final static String IM_QUEUE_DYNAMIC = "queue-name-im-dynamic";
    public final static String IM_QUEUE_TASK_STATUS = "queue-name-task-status";
    public final static String IM_QUEUE_CIRCLE = "queue-name-im-circle";
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Queue queueRedDot() {
        return new Queue(IM_QUEUE_RED_DOT);
    }

    @Bean
    public Queue queueDynamic() {
        return new Queue(IM_QUEUE_DYNAMIC);
    }

    @Bean
    public Queue queueTaskStatus() {
        return new Queue(IM_QUEUE_TASK_STATUS);
    }
    @Bean
    public Queue queueCircle() {
        return new Queue(IM_QUEUE_CIRCLE);
    }

}

