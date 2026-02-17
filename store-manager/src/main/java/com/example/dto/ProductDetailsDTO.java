package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product details information")
public class ProductDetailsDTO {

    @Schema(description = "Product title", example = "Essence Mascara Lash Princess", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Product description", example = "Popular mascara known for its volumizing effect")
    private String description;

    @Schema(description = "Product price", example = "9.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Discount percentage", example = "7.17")
    private BigDecimal discount;

    @Schema(description = "Average rating", example = "4.94")
    private BigDecimal rating;

    @Schema(description = "Available stock quantity", example = "5")
    private Integer stock;

    @Schema(description = "Brand name", example = "Essence")
    private String brand;

}
