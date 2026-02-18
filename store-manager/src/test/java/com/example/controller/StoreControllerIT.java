package com.example.controller;

import com.example.dto.ProductResponse;
import com.example.dto.ProductDetailsDTO;
import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class StoreControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void test() throws Exception {
        ProductResponse productResponse = new ProductResponse()
                .id(UUID.randomUUID())
                .category(ProductCategory.ELECTRONICS)
                .availabilityStatus(AvailabilityStatus.IN_STOCK)
                .productDetails(new ProductDetailsDTO()
                        .title("Test")
                        .price(BigDecimal.valueOf(100.00)));

        when(productService.findAll())
                .thenReturn(List.of(productResponse));

        this.mockMvc.perform(get("/products")
                        .header("Username", "user"))
                .andExpect(status().isOk());
    }
}
