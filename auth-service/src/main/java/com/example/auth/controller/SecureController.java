package com.example.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Example protected endpoints demonstrating role-based access control.
 */
@RestController
@RequestMapping("/api")
public class SecureController {

    /**
     * Accessible by any authenticated user (ROLE_USER or ROLE_ADMIN).
     */
    @GetMapping("/secure")
    public Map<String, Object> secureEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "You have access to a secured resource",
                "user", jwt.getSubject(),
                "role", jwt.getClaimAsString("role")
        );
    }

    /**
     * Accessible only by users with the ADMIN role.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, Object> adminEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "You have access to the admin resource",
                "user", jwt.getSubject()
        );
    }
}
