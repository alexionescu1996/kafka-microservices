package com.example.client.app;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FeignAuthInterceptor
        implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String user = "admin";
        String pass = "123456";

        String auth = user + ":" + pass;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        requestTemplate.header("Authorization", "Basic " + encoded);
    }
}
