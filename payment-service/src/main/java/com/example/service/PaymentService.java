package com.example.service;

import com.example.model.*;
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

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    final static Logger log =
            LoggerFactory.getLogger(PaymentService.class);

    private final KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;

    @Autowired
    public PaymentService(KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate) {
        this.paymentKafkaTemplate = paymentKafkaTemplate;
    }

    @KafkaListener(
            topics = "orders-topic",
            groupId = "orders-group"
    )
    public void sendPayment(ConsumerRecord<String, OrderCreatedEvent> record) {
        String key = record.key();
        OrderCreatedEvent order = record.value();

        log.info("Order processed key :: {}, value :: {}", key, order);

        simulatePayment(order);
    }

    private void simulatePayment(OrderCreatedEvent order) {
        try {
            Thread.sleep(1_000);
            boolean success = Math.random() < 0.85; // 85% success rate

            order.setStatus(success
                    ? OrderStatus.PAYMENT_SUCCESSFUL
                    : OrderStatus.PAYMENT_FAILED);

            log.info("payment :: {}, {}", order.getOrderId(), order.getStatus());
            publishPayment(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void publishPayment(OrderCreatedEvent order) {
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .orderId(order.getOrderId())
                .timestamp(new Date())
                .build();

        CompletableFuture<SendResult<String, PaymentEvent>> future =
                paymentKafkaTemplate.send(
                        "payments-topic",
                        paymentEvent.getOrderId(),
                        paymentEvent
                );

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("payment_error :: {ex}", exception);
            } else {
                ProducerRecord<String, PaymentEvent> producerRecord =
                        result.getProducerRecord();

                log.debug("success payment :: {}, {}",
                        producerRecord.key(), producerRecord.value());
            }
        });
    }



}

