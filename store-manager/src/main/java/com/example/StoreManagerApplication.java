package com.example;

import com.example.dto.ProductDetailsDTO;
import com.example.dto.ProductRequest;
import com.example.mapper.ProductMapper;
import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class StoreManagerApplication
        implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        List<ProductRequest> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new ProductRequest()
                    .productDetails(
                            new ProductDetailsDTO()
                                    .brand("Testing")
                                    .description("test")
                                    .price(BigDecimal.valueOf(1.23))
                    )
                    .availabilityStatus(AvailabilityStatus.IN_STOCK)
                    .category(ProductCategory.CLOTHING));
        }

        productService.insertBatch(list);

    }

    public static void main(String[] args) {
        SpringApplication.run(StoreManagerApplication.class, args);
    }

}
