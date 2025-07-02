package com.example.client.app.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        value = "dummyProducts",
        url = "https://dummyjson.com",
        contextId = "products-context"
)
public interface DummyJSONClient {

    @GetMapping(value = "/products")
    ProductResponse getProducts();


}