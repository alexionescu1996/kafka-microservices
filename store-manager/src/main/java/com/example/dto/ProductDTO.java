package com.example.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer id;
    private String title;
    private BigDecimal price;
    private String description;
    private Integer stock;
    private String category;

    public ProductDTO() {
    }

    public ProductDTO(Integer id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }
}
