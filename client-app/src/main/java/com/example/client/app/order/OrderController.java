package com.example.client.app.order;


import com.example.client.app.product.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderClient orderClient;

    @Autowired
    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @GetMapping("/hello")
    public String test() {
        log.info("Hello client ::");
        return orderClient.test();
    }

    @PostMapping("/add")
    ResponseEntity<String> createOrder(@RequestBody Order order) {
        return orderClient.postOrder(order);
    }


//    @GetMapping("/{id}")
//    ResponseEntity<?> findOrderById(@PathVariable Integer id);

}
