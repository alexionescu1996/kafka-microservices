package com.example.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.api.gateway.JwtUtils.decodeBasicToken;
import static com.example.api.gateway.JwtUtils.AuthUser;


@Component
public class AuthenticationFilter
        implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        log.info("testing ::");

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
            request = request
                    .mutate()
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


}
