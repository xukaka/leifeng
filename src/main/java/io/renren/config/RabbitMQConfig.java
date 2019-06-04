
package io.renren.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public final static String QUEUE_NAME = "queue-name-online";
    public final static String IM_QUEUE_DYNAMIC = "queue-name-im-dynamic";
    public final static String IM_QUEUE_TASK = "queue-name-im-task";
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
    public Queue queueDynamic() {
        return new Queue(IM_QUEUE_DYNAMIC);
    }

    @Bean
    public Queue queueTask() {
        return new Queue(IM_QUEUE_TASK);
    }
    @Bean
    public Queue queueCircle() {
        return new Queue(IM_QUEUE_CIRCLE);
    }

}

