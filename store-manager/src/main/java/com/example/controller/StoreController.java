package com.example.controller;

import com.example.dto.BulkProductRequest;
import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.service.ProductService;
import com.example.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "Product management operations")
public class StoreController {

    private final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final ProductService productService;

    @Operation(summary = "Get all products", operationId = "getProducts")
    @ApiResponse(responseCode = "200", description = "List of products",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> findAllProducts(@RequestHeader("Username") String username) {

        logger.info("user :: {}", username);
        final var list = productService.findAll();
        logger.info("Products list size :: {}", list.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @Operation(summary = "Get product by ID", operationId = "getProductById")
    @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> findProductById(
            @Parameter(description = "Product UUID", required = true) @PathVariable UUID id) {
        var product = productService.findById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }

    @Operation(summary = "Add a single product", operationId = "addProduct")
    @ApiResponse(responseCode = "201", description = "Product created")
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "409", description = "Duplicate product", content = @Content)
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

    @Operation(summary = "Bulk add products", operationId = "addProducts")
    @ApiResponse(responseCode = "200", description = "Products added",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))))
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> addProducts(@RequestBody BulkProductRequest bulkRequest) {

        for (ProductRequest p : bulkRequest.getItems()) {
            productService.insert(p);
        }

        List<ProductResponse> productList = productService.findAll();

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @Operation(summary = "Update product price", operationId = "updateProductPrice")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(
            @Parameter(description = "Product UUID", required = true) @PathVariable UUID id,
            @RequestBody BigDecimal newPrice,
            @RequestHeader("Username") String username) {

        if (username != null && !username.isEmpty()) {
            if (username.equals("user"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utils.validatePrice(newPrice);
        productService.updatePrice(id, newPrice);

        logger.info("Updating product with :: id {}, newPrice {}", id, newPrice);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Health check", operationId = "healthCheck")
    @ApiResponse(responseCode = "200", description = "Service is running")
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
