package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Token refresh request")
public record RefreshRequest(
        @Schema(description = "The refresh token obtained from login or previous refresh")
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {
}
