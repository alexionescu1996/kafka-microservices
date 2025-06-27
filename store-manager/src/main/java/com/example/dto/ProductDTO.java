package com.example.dto;

import com.example.model.Product;

import java.math.BigDecimal;

public class ProductDTO {


    Integer id;
    String title;
    BigDecimal price;
    String description;
    Integer stock;
    String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getCategory()
        );
    }

    public ProductDTO(Integer id, String title, BigDecimal price, String description, Integer stock, String category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.category = category;
    }

    public ProductDTO(Integer id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();

        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setCategory(productDTO.getCategory());

        return product;
    }
}
