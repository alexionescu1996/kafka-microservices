package com.example.client.app.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateOrderRequest {

    private Long productId;
    private String title;
    private Integer quantity;
    private BigDecimal totalPrice;
    private DeliveryAddress address;

}
