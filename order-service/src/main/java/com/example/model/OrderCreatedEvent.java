package com.example.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Getter
public class OrderCreatedEvent
        implements OrderEvent {

    private String orderId;
    private Long productId;
    private String title;
    private Integer quantity;
    private BigDecimal totalPrice;
    private DeliveryAddress address;
    private Date createdAt;
    private OrderStatus orderStatus;

    public static OrderCreatedEvent createEventFromReq(CreateOrderRequest request) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.orderId = UUID.randomUUID().toString();
        event.productId = request.getProductId();
        event.title = request.getTitle();
        event.quantity = request.getQuantity();
        event.address = request.getAddress();
        event.totalPrice = request.getTotalPrice();
        event.orderStatus = OrderStatus.ORDER_CREATED;
        event.createdAt = new Date();
        return event;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
