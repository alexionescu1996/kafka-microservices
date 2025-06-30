package com.example.api.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class ApiGatewayApplication {

    @Value("${services.products.url}")
    private String PRODUCTS_URL;

    @Value("${services.orders.url}")
    public String ORDERS_URL;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/orders/**")
                        .filters(f -> f
                                .addRequestHeader("X-API-GATEWAY", UUID.randomUUID().toString())
                                .circuitBreaker(config -> config
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/order"))
                        )
                        .uri(ORDERS_URL)
                )
                .route(p -> p
                        .path("/products/**")
                        .filters(f -> f
                                .addRequestHeader("X-API-GATEWAY", UUID.randomUUID().toString())
                                .circuitBreaker(config -> config
                                        .setName("productCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/product"))
                        )
                        .uri(PRODUCTS_URL))
                .build();
    }
}
