package com.example.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Getter
public class OrderCreatedEvent {

    private String orderId;
    private Long productId;
    private String title;
    private Integer quantity;
    private BigDecimal totalPrice;
    private DeliveryAddress address;
    private Date createdAt;
    private OrderStatus status;

    public static OrderCreatedEvent createEventFromReq(CreateOrderRequest request) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.orderId = UUID.randomUUID().toString();
        event.productId = request.getProductId();
        event.title = request.getTitle();
        event.quantity = request.getQuantity();
        event.address = request.getAddress();
        event.totalPrice = request.getTotalPrice();
        event.status = OrderStatus.ORDER_CREATED;
        event.createdAt = new Date();
        return event;
    }
}
