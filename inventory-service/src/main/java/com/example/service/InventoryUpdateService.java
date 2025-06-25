package com.example.service;

import com.example.model.Order;
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
            groupId = "inventory-group"
    )
    public void processOrder(Order order) {
        log.info("Received order for processing :: {}", order);
    }
}

