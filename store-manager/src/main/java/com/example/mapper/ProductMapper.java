package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO productDTO);
}