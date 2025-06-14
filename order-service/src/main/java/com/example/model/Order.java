package com.example.model;

import lombok.Data;

@Data
public class Order {

    private Long id;
    private String productName;
    private Integer quantity;
    private boolean isDelivered;

}
