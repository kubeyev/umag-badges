package com.badgers.umag.core.amq;

import com.badgers.umag.modules.sales.services.SaleService;
import com.badgers.umag.modules.supplies.models.requests.RecalculateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQReceiver {

    private final ObjectMapper mapper;
    private final SaleService service;

    @RabbitListener(queues = "${spring.rabbitmq.queuename}")
    public void receiveOne(String stringRequest) throws JsonProcessingException {
        RecalculateRequest request = mapper.readValue(stringRequest, RecalculateRequest.class);
        log.info("Successfully got command to recalculate: barcode: {} and date {}", request.getBarcode(), request.getStartDate());
        try {
            service.recalculateAllSalesByBarcode(LocalDateTime.parse(request.getStartDate()), request.getBarcode());
            log.info("Recalculated everything by barcode successfully");
        } catch (Exception e) {
            log.error("Cannot update database", e);
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.allQueueName}")
    public void receive(String go) {
        log.info("Successfully got command to recalculate, so {}", go);
        try {
            service.recalculateAllSales();
            log.info("Recalculated everything successfully");
        } catch (Exception e) {
            log.error("Cannot update database", e);
        }
    }
}