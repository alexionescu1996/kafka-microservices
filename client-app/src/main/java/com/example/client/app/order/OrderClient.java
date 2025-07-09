package com.example.client.app.order;

import com.example.client.app.interceptor.FeignAuthInterceptor;
import com.example.client.app.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "orderClient",
        url = "http://192.168.1.141:8090/orders",
        configuration = FeignAuthInterceptor.class
)
public interface OrderClient {

    @GetMapping("/hello")
    String test();

    @PostMapping
    ResponseEntity<String> postOrder(@RequestBody Order order);

    @GetMapping("/{id}")
    ResponseEntity<?> findOrderById(@PathVariable Integer id);

}