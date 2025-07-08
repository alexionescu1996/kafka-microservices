package com.example.service;

import com.example.model.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryUpdateService {

    final static Logger log =
            LoggerFactory.getLogger(InventoryUpdateService.class);

    @KafkaListener(
            topics = "orders-topic",
            groupId = "orders-group"
    )
    public void processOrder(ConsumerRecord<Long, Order> record) {
        Long key = record.key();
        log.info("Order processed :: {}, key :: {}", record.value(), key);
    }
}

