package com.badgers.umag.core.amq;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.AmqpTemplate;
import com.badgers.umag.core.amq.config.RabbitMQProperties;
import com.badgers.umag.modules.supplies.models.requests.RecalculateRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQSender implements MQSender {

    private final AmqpTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    @Async
    @Override
    public void send(RecalculateRequest data) {
        log.info("Preparing to send message: barcode {} and time:  {}", data.getBarcode(), data.getStartDate());
        rabbitTemplate.convertAndSend(rabbitMQProperties.getQueueName(), data);
        log.info("Successfully sent message to queue: {}", rabbitMQProperties.getQueueName());
    }

    @Async
    @Override
    public void send() {
        log.info("Preparing to recalculate all data");
        try {
            wait(10000);
        } catch (Exception e) {
            log.warn("Oops!");
        }
        rabbitTemplate.convertAndSend(rabbitMQProperties.getAllQueueName(), "go");
        log.info("Successfully sent message to queue: {}", rabbitMQProperties.getAllQueueName());
    }
}
