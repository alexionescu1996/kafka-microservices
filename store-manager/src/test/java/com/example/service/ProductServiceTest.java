package com.example.service;

import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.dto.ProductDetailsDTO;
import com.example.exception.DuplicateProductException;
import com.example.exception.ProductNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.model.AvailabilityStatus;
import com.example.model.Product;
import com.example.model.ProductCategory;
import com.example.model.ProductDetails;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Spy
    private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @InjectMocks
    private ProductService productService;

    private static final UUID PRODUCT_ID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");

    @Test
    void test_findAll() {

        when(repository.findAll())
                .thenReturn(findAll());

        List<ProductResponse> list = productService.findAll();

        assertEquals(3, list.size());
        verify(repository, times(1)).findAll();
        assertEquals("test0", list.getFirst().getProductDetails().getTitle());
    }

    @Test
    void test_findAll_when_empty_rs() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<ProductResponse> list = productService.findAll();
        assertEquals(0, list.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void test_findById_when_exists() {
        Product product = getProduct();

        when(repository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.findById(PRODUCT_ID);
        assertEquals("test", productResponse.getProductDetails().getTitle());
        assertEquals(BigDecimal.valueOf(1.25), productResponse.getProductDetails().getPrice());

        verify(repository, times(1)).findById(PRODUCT_ID);
    }

    @Test
    void test_findById_when_productNotFound() {
        when(repository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(PRODUCT_ID));
    }

    @Test
    void test_insert_new_product_when_success() {

        ProductRequest productRequest = new ProductRequest()
                .category(ProductCategory.ELECTRONICS)
                .productDetails(new ProductDetailsDTO()
                        .title("test")
                        .price(BigDecimal.valueOf(100.00)));

        when(repository.existsByTitle("test"))
                .thenReturn(false);

        productService.insert(productRequest);

        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void test_insert_new_product_when_duplicate() {
        ProductRequest productRequest = new ProductRequest()
                .category(ProductCategory.ELECTRONICS)
                .productDetails(new ProductDetailsDTO()
                        .title("test")
                        .price(BigDecimal.valueOf(100.00)));

        when(repository.existsByTitle("test"))
                .thenReturn(true);

        assertThrows(DuplicateProductException.class, () -> productService.insert(productRequest));
        verify(repository, times(0)).save(any(Product.class));
    }

    @Test
    void test_update_product() {
        productService.updatePrice(PRODUCT_ID, BigDecimal.valueOf(24.122));

        verify(repository, times(1))
                .updateProductPriceById(BigDecimal.valueOf(24.122), PRODUCT_ID);
    }

    private Product getProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);

        ProductDetails details = new ProductDetails();
        details.setTitle("test");
        details.setPrice(BigDecimal.valueOf(1.25));
        details.setProduct(product);
        product.setProductDetails(details);

        return product;
    }

    List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            product.setId(UUID.randomUUID());
            product.setCategory(ProductCategory.ELECTRONICS);

            ProductDetails details = new ProductDetails();
            details.setTitle("test" + i);
            details.setPrice(BigDecimal.valueOf(i * 10 + 3));
            details.setProduct(product);
            product.setProductDetails(details);

            list.add(product);
        }
        return list;
    }
}
