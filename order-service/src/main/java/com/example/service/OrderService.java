package com.example.service;

import com.example.model.Order;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    final static Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final KafkaTemplate<Long, Order> orderKafkaTemplate;
    private final KafkaTemplate<String, String> paymentKafkaTemplate;


    @Autowired
    public OrderService(
            KafkaTemplate<Long, Order> orderKafkaTemplate,
            KafkaTemplate<String, String> paymentKafkaTemplate) {

        this.orderKafkaTemplate = orderKafkaTemplate;
        this.paymentKafkaTemplate = paymentKafkaTemplate;
    }


    public void sendPaymentEvent(String paymentEvent) {
        Message<String> message = MessageBuilder
                .withPayload(paymentEvent)
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                .setHeader(KafkaHeaders.TOPIC, "payments-topic")
                .build();

        CompletableFuture<SendResult<String, String>> completableFuture = paymentKafkaTemplate.send(message);
        completableFuture.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("order_error :: {ex}", exception);

            } else {
                ProducerRecord<String, String> producerRecord = result.getProducerRecord();
                log.info("success payment :: {}, {}",
                        producerRecord.key(), producerRecord.value());
            }
        });
    }

    public void process(Order order) {

//        make some process, like status is not delivered
//        add Address

        CompletableFuture<SendResult<Long, Order>> future = orderKafkaTemplate.send(
                "orders-topic",
                order.getId(),
                order
        );

        future.whenComplete((result, ex) -> {
            ProducerRecord<Long, Order> pr = result.getProducerRecord();
            log.info("printing producer record :: key {}, value {}", pr.key(), pr.value());

            RecordMetadata recordMetadata = result.getRecordMetadata();
            if (ex != null) {
                log.error("order_error :: {ex}", ex);
            } else {
                log.info("offset :: {}, topic :: {}, partition :: {}, order :: {}",
                        recordMetadata.offset(), recordMetadata.topic(),
                        recordMetadata.partition(), order.getProductName());
            }
        });

    }

}
