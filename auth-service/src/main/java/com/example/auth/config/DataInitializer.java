package com.example.auth.config;

import com.example.auth.entity.Role;
import com.example.auth.entity.UserEntity;
import com.example.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds a default admin user on first startup if no users exist.
 * In production you would remove this or gate it behind a profile.
 */
@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                UserEntity admin = new UserEntity(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("admin123"),
                        Role.ROLE_ADMIN
                );
                userRepository.save(admin);
                log.info("Seeded default admin user (username: admin)");
            }
        };
    }
}
