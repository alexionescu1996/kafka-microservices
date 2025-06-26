package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.service.ProductService;
import com.example.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class StoreController {

    private final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final ProductService productService;

    public StoreController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllProducts() {
        var list = productService.findAll();
        logger.info("Products list size :: {}", list.size());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findProductById(@PathVariable Integer id) {
        var product = productService.findById(id);

        logger.info("Product found :: {}", product.name());

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        Utils.validateInput(productDTO.price(), productDTO.name());

        logger.info("Adding product :: name {}, price {}", productDTO.name(), productDTO.price());
        productService.insert(productDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable Integer id,
                                           @RequestBody BigDecimal newPrice) {
        Utils.validatePrice(newPrice);
        productService.update(id, newPrice);

        logger.info("Updating product with :: id {}, newPrice {}", id, newPrice);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/hello")
    public String test(@RequestHeader("X-API-GATEWAY") String apiGet) {

        System.out.println("products " + apiGet.toUpperCase());
        return "All good from Products!";
    }
}
