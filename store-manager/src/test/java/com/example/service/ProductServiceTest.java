package com.example.service;

import com.example.dto.ProductDTO;
import com.example.exception.DuplicateProductException;
import com.example.exception.ProductNotFoundException;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    @Test
    void test_findAll() {
        when(repository.findAll()).thenReturn(findAll());

        List<ProductDTO> list = productService.findAll();

        assertEquals(3, list.size());
        verify(repository, times(1)).findAll();
        assertEquals("test0", list.getFirst().getTitle());
    }

    @Test
    void test_findAll_when_empty_rs() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<ProductDTO> list = productService.findAll();
        assertEquals(0, list.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void test_findById_when_exists() {
        Product product = getProduct();

        when(repository.findById(1)).thenReturn(Optional.of(product));

        ProductDTO productDTO = productService.findById(1);
        assertEquals("test", productDTO.getTitle());
        assertEquals(BigDecimal.valueOf(1.25), productDTO.getPrice());

        verify(repository, times(1)).findById(1);
    }

    @Test
    void test_findById_when_productNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1));
    }

    @Test
    void test_insert_new_product_when_success() {
        ProductDTO productDTO = new ProductDTO(null, "test", BigDecimal.valueOf(1.25));

        when(repository.existsByTitle("test")).thenReturn(false);

        productService.insert(productDTO);

        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void test_insert_new_product_when_duplicate() {
        ProductDTO productDTO = new ProductDTO(null, "test", BigDecimal.valueOf(1.25));

        when(repository.existsByTitle("test")).thenReturn(true);

        assertThrows(DuplicateProductException.class, () -> productService.insert(productDTO));
        verify(repository, times(0)).save(any(Product.class));
    }

    @Test
    void test_update_product() {
        Product product = getProduct();
        when(repository.findById(1)).thenReturn(Optional.of(product));

        productService.update(1, BigDecimal.valueOf(24.122));

        assertEquals(BigDecimal.valueOf(24.122), product.getPrice());
    }

    private Product getProduct() {
        Product product = new Product();
        product.setId(1);
        product.setPrice(BigDecimal.valueOf(1.25));
        product.setTitle("test");
        return product;
    }

    List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            product.setId(i + 1);
            product.setTitle("test" + i);
            product.setPrice(BigDecimal.valueOf(i * 10 + 3));
            list.add(product);
        }
        return list;
    }
}
