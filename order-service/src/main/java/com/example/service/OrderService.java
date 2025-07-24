package com.example.service;

import com.example.model.CreateOrderRequest;
import com.example.model.Order;
import com.example.model.OrderCreatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    final static Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final KafkaTemplate<String, OrderCreatedEvent> orderKafkaTemplate;

    @Autowired
    public OrderService(KafkaTemplate<String, OrderCreatedEvent> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
    }

    public void handleCreateOrder(CreateOrderRequest order) {

        OrderCreatedEvent event = OrderCreatedEvent.createEventFromReq(order);

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                orderKafkaTemplate.send(
                        "orders-topic",
                        event.getOrderId(),
                        event
                );

        future.whenComplete((result, ex) -> {
            ProducerRecord<String, OrderCreatedEvent> producerRecord = result.getProducerRecord();

            log.info("printing producer record :: key {}, value {}",
                    producerRecord.key(), producerRecord.value());

            RecordMetadata recordMetadata = result.getRecordMetadata();
            if (ex != null) {
                log.error("order_error :: {ex}", ex);
            } else {
                log.debug("offset :: {}, topic :: {}, partition :: {}, orderId :: {}",
                        recordMetadata.offset(), recordMetadata.topic(),
                        recordMetadata.partition(), producerRecord.value().getOrderId());
            }
        });

    }

//TODO add listener for orders
}
