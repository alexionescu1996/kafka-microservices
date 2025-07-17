package com.example.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    final static Logger log =
            LoggerFactory.getLogger(PaymentService.class);

    @KafkaListener(
            topics = "payments-topic",
            groupId = "payments-group"
    )
    public void processPayment(ConsumerRecord<String, String> record) {
        String key = record.key();
        log.info("Order processed :: {}, key :: {}", record.value(), key);
    }
}

