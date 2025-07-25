package com.example.model;

public interface OrderEvent {

    String getOrderId();

    OrderStatus getOrderStatus();
}
