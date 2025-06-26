package com.example.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log-level")
public class LoggerController {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{level}")
    public ResponseEntity<String> update(@PathVariable String level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("ROOT").setLevel(Level.toLevel(level.toUpperCase()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Log level set to " + level);
    }
}
