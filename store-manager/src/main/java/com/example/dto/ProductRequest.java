package com.example.dto;

import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for creating a new product")
public class ProductRequest {

    @Schema(description = "Product category", example = "ELECTRONICS", requiredMode = Schema.RequiredMode.REQUIRED)
    private ProductCategory category;

    @Schema(description = "Product availability status", example = "IN_STOCK", requiredMode = Schema.RequiredMode.REQUIRED)
    private AvailabilityStatus availabilityStatus;

    @Schema(description = "Product details", requiredMode = Schema.RequiredMode.REQUIRED)
    private ProductDetailsDTO productDetails;

}
