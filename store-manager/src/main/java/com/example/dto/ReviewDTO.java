package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product review")
public class ReviewDTO {

    @Schema(description = "Review identifier", example = "1")
    private Integer id;

    @Schema(description = "Rating score", example = "5")
    private Integer rating;

    @Schema(description = "Review comment", example = "Excellent product!")
    private String comment;

    @Schema(description = "Review date", example = "2025-01-15T10:30:00+00:00")
    private OffsetDateTime date;

    @Schema(description = "Reviewer name", example = "John Doe")
    private String reviewerName;

    @Schema(description = "Reviewer email", example = "john@example.com")
    private String reviewerEmail;

}
