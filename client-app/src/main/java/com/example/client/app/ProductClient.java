package com.example.client.app;

import com.fasterxml.jackson.core.JsonParser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(
        value = "dummyProducts",
        url = "https://dummyjson.com"
)
public interface ProductClient {

    @GetMapping(value = "/products")
    ProductResponse getProducts();

}