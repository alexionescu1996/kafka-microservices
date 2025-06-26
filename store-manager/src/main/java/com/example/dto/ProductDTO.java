package com.example.dto;

import com.example.model.Product;

import java.math.BigDecimal;

public record ProductDTO(
        Integer id, String name, BigDecimal price
) {

    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
                product.getId(), product.getName(), product.getPrice()
        );
    }

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        return product;
    }
}
