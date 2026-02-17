package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing JWT tokens")
public record AuthResponse(
        @Schema(description = "Short-lived access token", example = "eyJhbGciOiJSUzI1NiJ9...")
        String accessToken,

        @Schema(description = "Long-lived refresh token", example = "eyJhbGciOiJSUzI1NiJ9...")
        String refreshToken,

        @Schema(description = "Token type", example = "Bearer")
        String tokenType,

        @Schema(description = "Access token lifetime in seconds", example = "900")
        long expiresIn
) {
    public AuthResponse(String accessToken, String refreshToken, long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }
}
