package com.example.service;

import com.example.model.Order;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    final static Logger log =
            LoggerFactory.getLogger(OrderService.class);

    public NewTopic ordersTopic;

    private final KafkaTemplate<Long, Order> kafkaTemplate;

    @Autowired
    public OrderService(KafkaTemplate<Long, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(Order order) {

//        make some process, like status is not delivered
//        add Address

        Message<Order> message = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.KEY, order.getId())
                .setHeader(KafkaHeaders.TOPIC, ordersTopic)
                .build();

        CompletableFuture<SendResult<Long, Order>> future = kafkaTemplate.send(
                ordersTopic.name(),
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
