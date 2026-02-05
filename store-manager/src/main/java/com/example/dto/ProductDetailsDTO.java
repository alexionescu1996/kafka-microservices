package com.example.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailsDTO {

    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal rating;
    private Integer stock;
    private String brand;

}
