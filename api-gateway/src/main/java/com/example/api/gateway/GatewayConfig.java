package com.example.api.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class GatewayConfig {

    @Value("${services.products.url}")
    private String PRODUCTS_URL;

    @Value("${services.orders.url}")
    private String ORDERS_URL;

    @Autowired
    private AuthenticationFilter authFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/orders/**")
                        .filters(f -> f
                                .filter(authFilter)
                                .addRequestHeader("X-API-GATEWAY", UUID.randomUUID().toString())
                                .circuitBreaker(config -> config
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/order")
                                )
                        )
                        .uri(ORDERS_URL)
                )
                .route(p -> p
                        .path("/products/**")
                        .filters(f -> f
                                .filter(authFilter)
                                .addRequestHeader("X-API-GATEWAY", UUID.randomUUID().toString())
                                .circuitBreaker(config -> config
                                        .setName("productCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/product")
                                )
                        )
                        .uri(PRODUCTS_URL))
                .build();
    }
}
