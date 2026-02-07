package com.example.controller;

import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.dto.ProductDetailsDTO;
import com.example.exception.DuplicateProductException;
import com.example.exception.GlobalExceptionHandler;
import com.example.exception.ProductNotFoundException;
import com.example.model.AvailabilityStatus;
import com.example.model.ProductCategory;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private StoreController storeController;

    private MockMvc mvc;

    private static final UUID PRODUCT_ID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(storeController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void test_get_all_when_success() throws Exception {
        when(productService.findAll()).thenReturn(productList());

        mvc.perform(get("/products")
                        .header("Username", "user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(productService, times(1))
                .findAll();
    }

    @Test
    void test_get_all_when_list_is_empty() throws Exception {
        when(productService.findAll())
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/products")
                        .header("Username", "user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(productService, times(1)).findAll();
    }

    @Test
    void test_get_all_when_exception() throws Exception {

        when(productService.findAll()).thenThrow(new RuntimeException(":("));

        mvc.perform(get("/products")
                        .header("Username", "user"))
                .andExpect(status().isInternalServerError());

        verify(productService, times(1)).findAll();
    }

    @Test
    void test_findById_when_success() throws Exception {
        ProductResponse product = ProductResponse.builder()
                .id(PRODUCT_ID)
                .category(ProductCategory.ELECTRONICS)
                .availabilityStatus(AvailabilityStatus.IN_STOCK)
                .productDetails(ProductDetailsDTO.builder()
                        .title("test")
                        .price(BigDecimal.valueOf(1.123))
                        .build())
                .build();

        when(productService.findById(PRODUCT_ID))
                .thenReturn(product);

        mvc.perform(get("/products/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(productService, times(1))
                .findById(PRODUCT_ID);
    }

    @Test
    void test_findById_when_invalid() throws Exception {
        when(productService.findById(PRODUCT_ID)).thenThrow(new ProductNotFoundException());

        mvc.perform(get("/products/" + PRODUCT_ID))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(PRODUCT_ID);
    }

    @Test
    void test_addProduct_when_success() throws Exception {
        mvc.perform(post("/products")
                        .header("Username", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertRequestBody))
                .andExpect(status().isCreated());

        verify(productService, times(1))
                .insert(any(ProductRequest.class));
    }

    @Test
    void test_addProduct_when_duplicate() throws Exception {
        doThrow(new DuplicateProductException())
                .when(productService).insert(any(ProductRequest.class));

        mvc.perform(post("/products")
                        .header("Username", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertRequestBody))
                .andExpect(status().isConflict());

        verify(productService, times(1)).insert(any(ProductRequest.class));
    }

    @Test
    void test_update_product_when_success() throws Exception {
        mvc.perform(put("/products/" + PRODUCT_ID)
                        .header("Username", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().isOk());

        verify(productService, times(1))
                .update(PRODUCT_ID, BigDecimal.valueOf(70.75));
    }

    @Test
    void test_update_product_when_invalid_price() throws Exception {
        mvc.perform(put("/products/" + PRODUCT_ID)
                        .header("Username", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("aa"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    String updateRequestBody = "70.75";

    String insertRequestBody = """
            {
                "category": "ELECTRONICS",
                "availabilityStatus": "IN_STOCK",
                "productDetails": {
                    "title": "test",
                    "price": 1231.2
                }
            }
            """;


    private List<ProductResponse> productList() {
        List<ProductResponse> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductResponse product = ProductResponse.builder()
                    .id(UUID.randomUUID())
                    .category(ProductCategory.ELECTRONICS)
                    .availabilityStatus(AvailabilityStatus.IN_STOCK)
                    .productDetails(ProductDetailsDTO.builder()
                            .title("test" + i)
                            .price(BigDecimal.valueOf(1.123 * i + 0.24))
                            .build())
                    .build();
            list.add(product);
        }

        return list;
    }
}
