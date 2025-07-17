package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    public String pay(@RequestBody String payment) {
        orderService.sendPaymentEvent(payment);
        return "ok";
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {

        orderService.process(order);

        return ResponseEntity.ok("Order processed");
    }

    @GetMapping("/hello")
    public String test(@RequestHeader("X-API-GATEWAY") String fromGateway,
                       @RequestHeader("Username") String username,
                       @RequestHeader("X-Rate-Limit-Remaining") Long avbTokens) {
        log.info("username :: {}", username);
        log.info("X-API-GATEWAY :: {}", fromGateway.toUpperCase());
        log.info("X-Rate-Limit-Remaining :: {}", avbTokens);
        return "All good from Orders!";
    }
}
