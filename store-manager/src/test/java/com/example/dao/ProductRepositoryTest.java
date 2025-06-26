package com.example.dao;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductRepository repository;

    @Test
    void test_insert() {
        Product product = new Product();
        product.setName("TESTING_NEW_PRODUCT");
        product.setPrice(BigDecimal.valueOf(23.11));

//      merge to context and flush
        Product saved = em.merge(product);
        em.flush();

        Product fromDB = repository.findById(saved.getId()).orElseThrow();

        assertEquals("TESTING_NEW_PRODUCT", fromDB.getName());
        assertEquals(BigDecimal.valueOf(23.11), product.getPrice());
    }
}
