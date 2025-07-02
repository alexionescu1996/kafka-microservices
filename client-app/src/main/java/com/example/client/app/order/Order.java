package com.example.client.app.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private Long id;
    private String productName;
    private Integer quantity;
    private Boolean isDelivered;

}
