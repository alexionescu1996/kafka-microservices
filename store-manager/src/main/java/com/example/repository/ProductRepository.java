package com.example.repository;

import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM ProductDetails pd WHERE pd.title = :title")
    Boolean existsByTitle(String title);

    @Modifying
    @Query("""
                update ProductDetails pd set pd.price= :newPrice where pd.id = :id
            """)
    void updateProductPriceById(@Param("newPrice") BigDecimal newPrice, @Param("id") UUID id);
}
