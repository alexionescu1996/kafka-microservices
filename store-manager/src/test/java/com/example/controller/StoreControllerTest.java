package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.exception.DuplicateProductException;
import com.example.exception.GlobalExceptionHandler;
import com.example.exception.ProductNotFoundException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(storeController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void test_get_all_when_success() throws Exception {
        when(productService.findAll()).thenReturn(productList());

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(productService, times(1)).findAll();
    }

    @Test
    void test_get_all_when_list_is_empty() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(productService, times(1)).findAll();
    }

    @Test
    void test_get_all_when_exception() throws Exception {

        when(productService.findAll()).thenThrow(new RuntimeException(":("));

        mvc.perform(get("/products"))
                .andExpect(status().isInternalServerError());

        verify(productService, times(1)).findAll();
    }

    @Test
    void test_findById_when_success() throws Exception {
        ProductDTO product = new ProductDTO(1, "test", BigDecimal.valueOf(1.124));

        when(productService.findById(1)).thenReturn(product);

        mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(productService, times(1)).findById(product.getId());
    }

    @Test
    void test_findById_when_invalid() throws Exception {
        when(productService.findById(1)).thenThrow(new ProductNotFoundException());

        mvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(1);
    }

//    @Test
    void test_addProduct_when_success() throws Exception {
        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertRequestBody))
                .andExpect(status().isCreated());

        verify(productService, times(1)).insert(any(ProductDTO.class));
    }

//    @Test
    void test_addProduct_when_duplicate() throws Exception {
        doThrow(new DuplicateProductException())
                .when(productService).insert(any(ProductDTO.class));

        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertRequestBody))
                .andExpect(status().isConflict());

        verify(productService, times(1)).insert(any(ProductDTO.class));
    }

    @Test
    void test_update_product_when_success() throws Exception {
        mvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().isOk());

        verify(productService, times(1)).update(1, BigDecimal.valueOf(70.75));
    }

    @Test
    void test_update_product_when_invalid_price() throws Exception {
        mvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("aa"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    String updateRequestBody = "70.75";

    String insertRequestBody = """
            {
            "name": "test",
            "price": 1231.2
            }
            """;


    private List<ProductDTO> productList() {
        return Arrays.asList(
                new ProductDTO(1, "test", BigDecimal.valueOf(1.2)),
                new ProductDTO(2, "test2", BigDecimal.valueOf(5.123)),
                new ProductDTO(3, "test3", BigDecimal.valueOf(2.512))
        );
    }
}
