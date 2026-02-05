package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_details")
public class ProductDetails {

    @Id
    private UUID id;

    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal rating;
    private Integer stock;
    private String brand;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Product product;
}
