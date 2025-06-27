package com.example.client.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class MainController {

    ProductClient productClient;

    ProductResponse response;

    StoreClient storeClient;

    public MainController(ProductClient productClient, StoreClient storeClient) {
        this.productClient = productClient;
        this.response = productClient.getProducts();
        System.out.println("done init " + this.response.getProducts().size());
        this.storeClient = storeClient;
    }

    @GetMapping
    String test() {
       return storeClient.postProduct(this.response);
    }

}
