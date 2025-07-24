package com.example.service;

import com.example.model.Order;
import com.example.model.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    final static Logger log =
            LoggerFactory.getLogger(PaymentService.class);

//    private final KafkaTemplate<String, OrderCreatedEvent> paymentKafkaTemplate;
//
//    @Autowired
//    public PaymentService(KafkaTemplate<String, OrderCreatedEvent> paymentKafkaTemplate) {
//        this.paymentKafkaTemplate = paymentKafkaTemplate;
//    }

    @KafkaListener(
            topics = "orders-topic",
            groupId = "orders-group"
    )
    public void sendPayment(ConsumerRecord<String, OrderCreatedEvent> record) {
        String key = record.key();
        OrderCreatedEvent order = record.value();

        log.info("Order processed key :: {}, value :: {}",
                key, order);

//      TODO  add payment logic here publish to payments-topic response
    }

//    public void sendPaymentEvent(String paymentEvent) {
//        Message<String> message = MessageBuilder
//                .withPayload(paymentEvent)
//                .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
//                .setHeader(KafkaHeaders.TOPIC, "payments-topic")
//                .build();
//
//        CompletableFuture<SendResult<String, String>> completableFuture = paymentKafkaTemplate.send(message);
//        completableFuture.whenComplete((result, exception) -> {
//            if (exception != null) {
//                log.error("order_error :: {ex}", exception);
//
//            } else {
//                ProducerRecord<String, String> producerRecord = result.getProducerRecord();
//                log.info("success payment :: {}, {}",
//                        producerRecord.key(), producerRecord.value());
//            }
//        });
//    }


}

