package com.example.dto;

import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private ProductCategory category;
    private AvailabilityStatus availabilityStatus;
    private ProductDetailsDTO productDetails;

}
