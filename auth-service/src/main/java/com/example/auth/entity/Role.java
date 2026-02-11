package com.example.auth.entity;

/**
 * Application roles used for authorization decisions.
 * Stored as a string in the user table and mapped to Spring Security granted authorities.
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
