package com.example.utils;

import java.math.BigDecimal;

public class Utils {

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    public static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }
    }

    public static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid title");
        }
    }

    public static void validateInput(BigDecimal price, String title) {
        validatePrice(price);
        validateTitle(title);
    }

}
