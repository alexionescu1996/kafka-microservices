package com.example.dto;

import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response payload representing a product")
public class ProductResponse {

    @Schema(description = "Unique product identifier", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID id;

    @Schema(description = "Product category", example = "ELECTRONICS")
    private ProductCategory category;

    @Schema(description = "Product availability status", example = "IN_STOCK")
    private AvailabilityStatus availabilityStatus;

    @Schema(description = "Product details")
    private ProductDetailsDTO productDetails;

    @Schema(description = "Product reviews")
    private Set<ReviewDTO> reviews;

}
