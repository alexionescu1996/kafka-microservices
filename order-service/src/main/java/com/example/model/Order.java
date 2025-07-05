package com.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order {

    private Long id;
    private String productName;
    private Integer quantity;
    private Boolean isDelivered;

}
