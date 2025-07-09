package com.example.api.gateway;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.api.gateway.JwtUtils.decodeBasicToken;
import static com.example.api.gateway.JwtUtils.AuthUser;


@Component
public class AuthenticationFilter
        implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        log.info("gatewayFilter ::");

        ServerHttpRequest request = exchange.getRequest();

        if (authMissing(request)) {
            return onError(exchange);
        }

        final String token = request
                .getHeaders()
                .getOrEmpty("Authorization")
                .getFirst()
                .replace("Basic ", "");

        Optional<AuthUser> authUserOptional = decodeBasicToken(token);
        if (authUserOptional.isEmpty()) {
            return onError(exchange);
        } else {
            AuthUser authUser = authUserOptional.get();
            log.info("authUser :: {}", authUser);

            Bucket bucket = buckets.computeIfAbsent(authUser.username(), this::createBucket);

            if (!bucket.tryConsume(1)) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }

            request = request
                    .mutate()
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .header("Username", authUser.username())
                    .header("Password", authUser.password())
                    .build();

            return chain.filter(
                    exchange
                            .mutate()
                            .request(request)
                            .build()
            );
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private Bucket createBucket(String username) {

        int maxRate = switch (username) {
            case "user" -> 1;
            case "manager" -> 2;
            case "admin" -> 3;
            default -> 0;
        };

        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(maxRate)
                .refillIntervally(maxRate, Duration.ofSeconds(5))
                .build();

        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }

}
