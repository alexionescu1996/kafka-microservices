package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Wrapper for bulk product creation")
public class BulkProductRequest {

    @Schema(description = "List of products to create", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ProductRequest> items;

}
