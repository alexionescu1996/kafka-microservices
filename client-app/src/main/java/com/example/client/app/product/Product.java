package com.example.client.app.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Product {

    private Integer id;
    private String title;
    private BigDecimal price;
    private String description;
    private Integer stock;
    private String category;

}
