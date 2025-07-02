package com.example.client.app.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class FeignAuthInterceptor
        implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignAuthInterceptor.class);

    private final static List<AuthUser> authUsers = Arrays.asList(
            new AuthUser("user", "123456"),
            new AuthUser("admin", "123456"),
            new AuthUser("manager", "123456"));

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Collections.shuffle(authUsers);

        AuthUser authUser = authUsers.getFirst();
        String user = authUser.username();
        String pass = authUser.password();

        log.info("user pass :: {}, {}", user, pass);

        String auth = user + ":" + pass;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        requestTemplate.header("Authorization", "Basic " + encoded);
    }
}
