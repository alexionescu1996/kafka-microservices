package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.dto.ProductResponse;
import com.example.mapper.ProductMapper;
import com.example.service.ProductService;
import com.example.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class StoreController {

    private final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final ProductMapper mapper;
    private final ProductService productService;

    public StoreController(ProductMapper mapper, ProductService productService) {
        this.mapper = mapper;
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllProducts(@RequestHeader("Username") String username) {

        logger.info("user :: {}", username);
        var list = productService.findAll();
        logger.info("Products list size :: {}", list.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findProductById(@PathVariable Integer id) {
        var product = productService.findById(id);

//        logger.info("Product found :: {}", product.getTitle());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }

    //    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO,
                                        @RequestHeader("Username") String username) {

        if (username != null && !username.isEmpty()) {
            if (username.equals("user"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Utils.validateInput(productDTO.getPrice(), productDTO.getTitle());

        logger.info("Adding product :: name {}, price {}",
                productDTO.getTitle(), productDTO.getPrice());

        productService.insert(productDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProducts(@RequestBody ProductResponse productResponse) {

        List<ProductDTO> products = productResponse.getProducts()
                .stream()
                .map(mapper::toDTO)
                .toList();

        for (ProductDTO p : products) {
            productService.insert(p);
        }

        List<ProductDTO> productDTOLit = productService.findAll();

        return new ResponseEntity<>(productDTOLit, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable Integer id,
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
