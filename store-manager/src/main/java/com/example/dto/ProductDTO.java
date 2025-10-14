package com.example.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer id;
    private String title;
    private BigDecimal price;
    private String description;
    private Integer stock;
    private String category;

}
