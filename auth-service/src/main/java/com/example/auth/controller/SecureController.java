package com.example.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Secure Resources", description = "Protected endpoints requiring JWT authentication")
public class SecureController {

    @GetMapping("/secure")
    @Operation(summary = "Access secured resource", description = "Accessible by any authenticated user")
    @ApiResponse(responseCode = "200", description = "Access granted")
    @ApiResponse(responseCode = "401", description = "Missing or invalid token")
    public Map<String, Object> secureEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "You have access to a secured resource",
                "user", jwt.getSubject(),
                "role", jwt.getClaimAsString("role")
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Access admin resource", description = "Accessible only by users with ROLE_ADMIN")
    @ApiResponse(responseCode = "200", description = "Admin access granted")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    public Map<String, Object> adminEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "You have access to the admin resource",
                "user", jwt.getSubject()
        );
    }
}
