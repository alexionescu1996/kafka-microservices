package com.example.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;


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

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                '}';
    }
}
