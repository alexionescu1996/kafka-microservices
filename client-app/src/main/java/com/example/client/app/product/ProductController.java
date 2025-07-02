package com.example.client.app.product;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class);

    ProductResponse response;

    private final DummyJSONClient dummyJSONClient;
    private final StoreClient storeClient;

    @Autowired
    public ProductController(DummyJSONClient dummyJSONClient,
                             StoreClient storeClient) {

        this.dummyJSONClient = dummyJSONClient;
        this.storeClient = storeClient;
    }

    @PostConstruct
    void init() {
        this.response = dummyJSONClient.getProducts();

        logger.info("init done, dummy products size :: {}",
                this.response.getProducts().size());
    }

    @PostMapping
    String postProducts() {
        return storeClient.postProduct(this.response);
    }

    @GetMapping
    ResponseEntity<?> findAll() {
        return storeClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Integer id) {
        return storeClient.findProductById(id);
    }


}
