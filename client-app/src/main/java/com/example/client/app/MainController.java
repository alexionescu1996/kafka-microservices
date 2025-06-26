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

    public MainController(ProductClient productClient) {
        this.productClient = productClient;
        this.response = productClient.getProducts();
    }

    @GetMapping(value = "/products")
    ResponseEntity<?> getProducts() {
        List<Product> list = response.getProducts();

        System.out.println(list.size());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
