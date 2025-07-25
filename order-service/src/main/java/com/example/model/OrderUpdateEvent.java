package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder
@ToString
public class OrderUpdateEvent
        implements OrderEvent {

    private String orderId;
    private OrderStatus orderStatus;
    private Date updatedAt;
}
