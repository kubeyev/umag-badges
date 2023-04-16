package com.badgers.umag.core.amq.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final AmqpAdmin amqpAdmin;

    @Value("${spring.rabbitmq.queuename}")
    String queueName;

    @Value("${spring.rabbitmq.allQueueName}")
    public String allQueueName;

    @PostConstruct
    public void createQueues() {
        amqpAdmin.declareQueue(new Queue(queueName, false));
        amqpAdmin.declareQueue(new Queue(allQueueName, false));
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(AbstractConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
