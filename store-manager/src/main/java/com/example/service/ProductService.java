package com.example.service;

import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.exception.DuplicateProductException;
import com.example.exception.ProductNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.model.ProductDetails;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        final var products = productRepository.findAll();
        log.info("products :: {}", products);
        if (products.isEmpty())
            return Collections.emptyList();

        log.info("findAll service");

        return products.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ProductResponse findById(final UUID id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return mapper.toResponse(product);
    }

    @Transactional
    public void insert(ProductRequest productRequest) {
        if (productRequest.getProductDetails() != null && productRequest.getProductDetails().getTitle() != null) {
            Boolean isPresent = productRepository.existsByTitle(productRequest.getProductDetails().getTitle());

            if (isPresent)
                throw new DuplicateProductException();

            Product newProduct = mapper.toEntity(productRequest);
            ProductDetails details = mapper.toDetailsEntity(productRequest.getProductDetails());
            details.setProduct(newProduct);
            newProduct.setProductDetails(details);

            productRepository.save(newProduct);
        }
    }

    @Transactional
    public void update(UUID id, BigDecimal newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getProductDetails() != null) {
            product.getProductDetails().setPrice(newPrice);
        }
    }
}
