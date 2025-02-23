package com.kafka_tutorial;

import java.util.Date;

public record ErrorMessage(
        Date timestamp,
        String message,
        String details
) {
}
