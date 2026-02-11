package com.example.auth.service;

import com.example.auth.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Responsible for generating signed JWT access and refresh tokens.
 *
 * Access tokens contain the user's role as a claim and have a short lifetime.
 * Refresh tokens carry a dedicated "token_type" claim and a longer lifetime;
 * they contain no role information so they cannot be used as access tokens.
 */
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long accessTokenExpirationSeconds;
    private final long refreshTokenExpirationSeconds;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${jwt.access-token-expiration:900}") long accessTokenExpirationSeconds,
            @Value("${jwt.refresh-token-expiration:86400}") long refreshTokenExpirationSeconds
    ) {
        this.jwtEncoder = jwtEncoder;
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
        this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
    }

    /**
     * Generate a short-lived access token that includes the user's role.
     */
    public String generateAccessToken(UserEntity user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpirationSeconds))
                .claim("user_id", user.getId().toString())
                .claim("role", user.getRole().name())
                .claim("token_type", "access")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Generate a long-lived refresh token. It cannot be used as an access token
     * because it lacks role claims and its token_type is "refresh".
     */
    public String generateRefreshToken(UserEntity user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshTokenExpirationSeconds))
                .claim("user_id", user.getId().toString())
                .claim("token_type", "refresh")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }
}
