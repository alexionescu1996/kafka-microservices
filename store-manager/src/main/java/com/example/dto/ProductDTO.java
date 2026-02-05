package com.example.dto;

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
    private String category;
    private String availabilityStatus;
    private ProductDetailsDTO productDetails;
    private Set<ReviewDTO> reviews;

}
