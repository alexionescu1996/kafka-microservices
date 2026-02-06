package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.dto.ReviewDTO;
import com.example.model.Product;
import com.example.model.ProductDetails;
import com.example.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    @Mapping(source = "productDetails.title", target = "title")
    @Mapping(source = "productDetails.description", target = "description")
    @Mapping(source = "productDetails.price", target = "price")
    @Mapping(source = "productDetails.discount", target = "discountPercentage")
    @Mapping(source = "productDetails.rating", target = "rating")
    @Mapping(source = "productDetails.stock", target = "stock")
    @Mapping(source = "productDetails.brand", target = "brand")
    @Mapping(target = "id", ignore = true)
    ProductDTO toDTO(Product product);

    ReviewDTO toReviewDTO(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productDetails", ignore = true)
    @Mapping(target = "availabilityStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(source = "discountPercentage", target = "discount")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductDetails toDetailsEntity(ProductDTO productDTO);

    @Mapping(target = "id", ignore = true)
    Review toReviewEntity(ReviewDTO reviewDTO);
}
