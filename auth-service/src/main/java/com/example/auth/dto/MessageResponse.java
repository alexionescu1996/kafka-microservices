package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Simple message response")
public record MessageResponse(
        @Schema(description = "Response message")
        String message
) {
}
