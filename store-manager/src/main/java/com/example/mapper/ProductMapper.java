package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.dto.ProductDetailsDTO;
import com.example.dto.ReviewDTO;
import com.example.model.Product;
import com.example.model.ProductDetails;
import com.example.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    ProductDetailsDTO toDetailsDTO(ProductDetails productDetails);

    ReviewDTO toReviewDTO(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productDetails", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductDetails toDetailsEntity(ProductDetailsDTO productDetailsDTO);

    @Mapping(target = "id", ignore = true)
    Review toReviewEntity(ReviewDTO reviewDTO);
}
