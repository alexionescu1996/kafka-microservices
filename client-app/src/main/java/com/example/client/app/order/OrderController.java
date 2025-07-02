package com.example.client.app.order;


import com.example.client.app.product.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderClient orderClient;

    @Autowired
    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @GetMapping("/hello")
    public String test() {
        return orderClient.test();
    }

    @PostMapping("/add")
    ResponseEntity<?> createOrder(@RequestBody Order order) {
        return orderClient.postOrder(order);
    }


//    @GetMapping("/{id}")
//    ResponseEntity<?> findOrderById(@PathVariable Integer id);

}
