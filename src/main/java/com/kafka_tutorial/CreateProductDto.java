package com.kafka_tutorial;

import java.math.BigDecimal;

public record CreateProductDto(
        String title,
        BigDecimal price,
        Integer quantity) {
}
