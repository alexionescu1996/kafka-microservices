package com.example.service;

import com.example.model.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class InventoryUpdateService {

    final static Logger log =
            LoggerFactory.getLogger(InventoryUpdateService.class);

    @KafkaListener(
            topics = "orders-topic",
            groupId = "inventory-group"
    )
    public void processOrder(ConsumerRecord<String, Order> record) {

        log.info("Order processed :: {}, key :: {}", record.value(), record.key());
    }
}

