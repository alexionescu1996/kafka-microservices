package com.example.service;

import com.example.model.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    final static Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final KafkaTemplate<String, OrderCreatedEvent> orderKafkaTemplate;
    private final KafkaTemplate<String, OrderUpdateEvent> orderUpdateKafkaTemplate;

    @Autowired
    public OrderService(KafkaTemplate<String, OrderCreatedEvent> orderKafkaTemplate,
                        KafkaTemplate<String, OrderUpdateEvent> orderUpdateKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.orderUpdateKafkaTemplate = orderUpdateKafkaTemplate;
    }


    public void handleCreateOrder(CreateOrderRequest order) {

        OrderCreatedEvent event = OrderCreatedEvent.createEventFromReq(order);

        ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>(
                "orders-topic",
                event.getOrderId(),
                event
        );

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                orderKafkaTemplate.send(record);

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

    //    TODO add payment consumer config
    @KafkaListener(
            topics = "payments-topic",
            groupId = "payments-group"
    )
    public void updatePaymentStatus(ConsumerRecord<String, PaymentEvent> record) {
        String key = record.key();
        PaymentEvent order = record.value();

        log.info("Order payment key :: {}, value :: {}", key, order);

        updateOrderPayment(order);
    }

    private void updateOrderPayment(PaymentEvent order) {
        var paymentStatus = order.getStatus();

        OrderUpdateEvent orderUpdateEvent = OrderUpdateEvent.builder()
                .orderStatus(paymentStatus.equals(PaymentStatus.PAYMENT_FAILED)
                        ? OrderStatus.PAYMENT_FAILED
                        : OrderStatus.PAYMENT_SUCCESSFUL)
                .orderId(order.getOrderId())
                .updatedAt(new Date())
                .build();

        CompletableFuture<SendResult<String, OrderUpdateEvent>> future =
                orderUpdateKafkaTemplate.send(
                        "orders-update-topic",
                        orderUpdateEvent.getOrderId(),
                        orderUpdateEvent
                );

        future.whenComplete((result, ex) -> {
            ProducerRecord<String, OrderUpdateEvent> producerRecord = result.getProducerRecord();

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

}
