package com.example.dto;

import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import lombok.*;

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
    private ProductCategory category;
    private AvailabilityStatus availabilityStatus;
    private ProductDetailsDTO productDetails;
    private Set<ReviewDTO> reviews;

}
