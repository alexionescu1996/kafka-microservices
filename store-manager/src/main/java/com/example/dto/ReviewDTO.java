package com.example.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Integer id;
    private Integer rating;
    private String comment;
    private OffsetDateTime date;
    private String reviewerName;
    private String reviewerEmail;

}
