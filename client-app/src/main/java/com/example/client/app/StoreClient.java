package com.example.client.app;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        value = "storeClient",
        url = "http://localhost:8091",
        configuration = FeignAuthInterceptor.class
)
public interface StoreClient {

    @PostMapping("/products/add")
    String postProduct(ProductResponse response);

    @GetMapping("/products")
    List<Product> list();
}

//Feign.builder()
//  .requestInterceptor(new AuthInterceptor(new ApiAuthorisationService()))
//        .encoder(new GsonEncoder())
//        .decoder(new GsonDecoder())
//        .logger(new Slf4jLogger(type))
//        .logLevel(Logger.Level.HEADERS)
//  .target(BookClient.class, "http://localhost:8081/api/books");
