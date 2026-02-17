package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User login request")
public record LoginRequest(
        @Schema(description = "Username", example = "john_doe")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "Password", example = "securepass123")
        @NotBlank(message = "Password is required")
        String password
) {
}
