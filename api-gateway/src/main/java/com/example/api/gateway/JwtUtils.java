package com.example.api.gateway;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class JwtUtils {

    public record AuthUser(String username, String password) {
    }

    private final static Map<String, String> credentials = Map.of(
            "user", "123456",
            "admin", "123456",
            "manager", "123456"
    );

    public static Optional<AuthUser> decodeBasicToken(String token) {

        String decoded = new String(Base64.getDecoder().decode(token));
        String[] parts = decoded.split(":");

        String username = parts[0];
        String password = parts[1];

        return isValidUser(username, password) ?
                Optional.of(new AuthUser(username, password)) :
                Optional.empty();
    }

    private static boolean isValidUser(String username,
                                       String password) {
        return credentials.containsKey(username)
                && credentials.get(username).equals(password);
    }
}
