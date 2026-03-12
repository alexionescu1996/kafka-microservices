package com.example.controller;

import com.example.api.ProductsApi;
import com.example.dto.BulkProductRequest;
import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.service.ProductService;
import com.example.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class StoreController implements ProductsApi {

    private final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final ProductService productService;

    @Override
    public ResponseEntity<List<ProductResponse>> getProducts(String username) {

        logger.info("user :: {}", username);
        final var list = productService.findAll();
        logger.info("Products list size :: {}", list.size());

        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Override
    public ResponseEntity<Void> addProduct(String username, ProductRequest productRequest) {

        if ("user".equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Utils.validateInput(productRequest.getProductDetails().getPrice(),
                productRequest.getProductDetails().getTitle());

        logger.info("Adding product :: category {}", productRequest.getCategory());

        productService.insert(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductResponse>> addProducts(BulkProductRequest bulkProductRequest) {

        for (ProductRequest p : bulkProductRequest.getItems()) {
            productService.insert(p);
        }

        List<ProductResponse> productList = productService.findAll();

        return ResponseEntity.ok(productList);
    }

    @Override
    public ResponseEntity<Void> updateProductPrice(UUID id, String username, BigDecimal body) {

        if ("user".equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Utils.validatePrice(body);
        productService.updatePrice(id, body);

        logger.info("Updating product with :: id {}, newPrice {}", id, body);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/hello")
    public String test(@RequestHeader("X-API-GATEWAY") String apiGet,
                       @RequestHeader("Username") String username,
                       @RequestHeader("X-Rate-Limit-Remaining") Long avbTokens) {

        logger.info("X-API-GATEWAY :: {}", apiGet);
        logger.info("Username :: {}", username);
        logger.info("X-Rate-Limit-Remaining :: {}", avbTokens);

        return "All good from Products!";
    }
}
