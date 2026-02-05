package com.example.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private UUID id;
    private String category;
    private String availabilityStatus;

    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal rating;
    private Integer stock;
    private String brand;

    private Set<ReviewDTO> reviews;

}
