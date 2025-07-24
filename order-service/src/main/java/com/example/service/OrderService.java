package com.example.service;

import com.example.model.Order;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    final static Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final KafkaTemplate<Long, Order> orderKafkaTemplate;

    @Autowired
    public OrderService(
            KafkaTemplate<Long, Order> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
    }

    public void sendOrder(Order order) {

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
