package com.example.service;

import com.example.dto.ProductDTO;
import com.example.exception.DuplicateProductException;
import com.example.exception.ProductNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public List<ProductDTO> findAll() {
        var products = productRepository.findAll();
        log.info("products :: {}", products);
        if (products.isEmpty())
            return Collections.emptyList();

        log.info("findAll service");

        return products.stream()
                .map(mapper::toDTO)
                .toList();

//        return products.stream()
//                .map(ProductDTO::fromEntity)
//                .toList();
    }

    public ProductDTO findById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());

        return mapper.toDTO(product);
    }

    @Transactional
    public void insert(ProductDTO productDTO) {
        if (productDTO.getTitle() != null) {
            Boolean isPresent = productRepository.existsByTitle(productDTO.getTitle());

            if (isPresent)
                throw new DuplicateProductException();

            Product newProduct = mapper.toEntity(productDTO);
            productRepository.save(newProduct);
        }
    }

    @Transactional
    public void update(Integer id, BigDecimal newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.setPrice(newPrice);
    }
}
