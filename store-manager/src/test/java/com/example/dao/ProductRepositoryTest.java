package com.example.dao;

import com.example.model.AvailabilityStatus;
import com.example.model.Product;
import com.example.model.ProductCategory;
import com.example.model.ProductDetails;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductRepository repository;

    @Test
    void test_insert() {
        Product product = new Product();
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);

        ProductDetails details = new ProductDetails();
        details.setTitle("TESTING_NEW_PRODUCT");
        details.setPrice(BigDecimal.valueOf(23.11));
        details.setProduct(product);
        product.setProductDetails(details);

        Product saved = em.persistAndFlush(product);

        Product fromDB = repository.findById(saved.getId()).orElseThrow();

        assertNotNull(fromDB.getId());
        assertNotNull(fromDB.getProductDetails());
        assertEquals("TESTING_NEW_PRODUCT", fromDB.getProductDetails().getTitle());
        assertEquals(BigDecimal.valueOf(23.11), fromDB.getProductDetails().getPrice());
    }
}
