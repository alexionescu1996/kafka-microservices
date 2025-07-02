package com.example.client.app.order;

import com.example.client.app.interceptor.FeignAuthInterceptor;
import com.example.client.app.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "orderClient",
        url = "http://localhost:8091/orders",
        configuration = FeignAuthInterceptor.class
)
public interface OrderClient {

    @PostMapping("/add")
    ResponseEntity<?> postOrder(Order response);

    @GetMapping("/hello")
    public String test();


    @GetMapping("/{id}")
    ResponseEntity<?> findOrderById(@PathVariable Integer id);

}