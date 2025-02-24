package com.kafka_tutorial;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {
    String productId;
    String title;
    BigDecimal price;
    Integer quantity;
}
