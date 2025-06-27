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
        System.out.println("done init");
        this.storeClient = storeClient;
    }

    @GetMapping(value = "/products")
    ResponseEntity<?> getProducts() {
        List<Product> list = response.getProducts();

        System.out.println(list.size());

        storeClient.postProduct(response);

        List<Product> k = storeClient.list();
        System.out.println(k);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
