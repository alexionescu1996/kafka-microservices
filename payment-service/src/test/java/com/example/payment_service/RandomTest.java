package com.example.payment_service;

import org.junit.jupiter.api.Test;

public class RandomTest {

    @Test
    void test() {
//        Math.random() returns a value between 0 and 1 (including 0, but not 1: [0, 1)

        for (int i = 0; i < 10; i++) {
            double random = Math.random();
            boolean success = random < 0.85; // 85%
            System.out.println(random);
        }
    }
}
