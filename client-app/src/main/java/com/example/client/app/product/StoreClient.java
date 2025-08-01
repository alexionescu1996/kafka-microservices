package com.example.client.app.product;

import com.example.client.app.interceptor.FeignAuthInterceptor;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "storeClient",
        url = "http://192.168.1.141:8090/products",
        configuration = FeignAuthInterceptor.class
)
public interface StoreClient {

    @PostMapping("/add")
    String postProduct(ProductResponse response);

    @GetMapping
    ResponseEntity<?> findAll();

    @GetMapping("/{id}")
    ResponseEntity<?> findProductById(@PathVariable Integer id);

}