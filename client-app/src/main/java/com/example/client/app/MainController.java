package com.example.client.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class MainController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    ProductClient productClient;
    ProductResponse response;
    StoreClient storeClient;

    public MainController(ProductClient productClient, StoreClient storeClient) {
        this.productClient = productClient;
        this.response = productClient.getProducts();
        logger.info("init done, dummy products size :: {}",
                this.response.getProducts().size());
        this.storeClient = storeClient;
    }

    @GetMapping
    String postProducts() {
        return storeClient.postProduct(this.response);
    }

}
