package com.badgers.umag.core.amq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class RabbitMQProperties {

    @Value("${spring.rabbitmq.queuename}")
    public String queueName;

    @Value("${spring.rabbitmq.allQueueName}")
    public String allQueueName;
}
