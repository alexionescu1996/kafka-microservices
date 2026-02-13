package com.example.controller;

import com.example.dto.BulkProductRequest;
import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.service.ProductService;
import com.example.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class StoreController {

    private final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> findAllProducts(@RequestHeader("Username") String username) {

        logger.info("user :: {}", username);
        final var list = productService.findAll();
        logger.info("Products list size :: {}", list.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> findProductById(@PathVariable UUID id) {
        var product = productService.findById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productRequest,
                                        @RequestHeader("Username") String username) {

        if (username != null && !username.isEmpty()) {
            if (username.equals("user"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (productRequest.getProductDetails() != null) {
            Utils.validateInput(productRequest.getProductDetails().getPrice(),
                    productRequest.getProductDetails().getTitle());
        }

        logger.info("Adding product :: category {}", productRequest.getCategory());

        productService.insert(productRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> addProducts(@RequestBody BulkProductRequest bulkRequest) {

        for (ProductRequest p : bulkRequest.getItems()) {
            productService.insert(p);
        }

        List<ProductResponse> productList = productService.findAll();

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable UUID id,
                                           @RequestBody BigDecimal newPrice,
                                           @RequestHeader("Username") String username) {

        if (username != null && !username.isEmpty()) {
            if (username.equals("user"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utils.validatePrice(newPrice);
        productService.update(id, newPrice);

        logger.info("Updating product with :: id {}, newPrice {}", id, newPrice);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/hello")
    public String test(@RequestHeader("X-API-GATEWAY") String apiGet,
                       @RequestHeader("Username") String username,
                       @RequestHeader("X-Rate-Limit-Remaining") Long avbTokens) {

        logger.info("X-API-GATEWAY :: {}", apiGet);
        logger.info("Username :: {}", username);
        logger.info("X-Rate-Limit-Remaining :: {}", avbTokens);

        return "All good from Products!";
    }
}
