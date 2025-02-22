package com.kafka_tutorial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final KafkaTemplate<String, ProductCreateEvent> kafkaTemplate;

    public String createProduct(CreateProductDto createProductDto) {
        return Optional.of(UUID.randomUUID().toString())
                .map(uuid -> new ProductCreateEvent(
                        uuid,
                        createProductDto.title(),
                        createProductDto.price(),
                        createProductDto.quantity()
                ))
                .map(productCreateEvent -> {
                    CompletableFuture<SendResult<String, ProductCreateEvent>> future =
                            kafkaTemplate.send(
                                    "product-created-event-topic",
                                    productCreateEvent.getProductId(),
                                    productCreateEvent
                            );
                    future.whenComplete((result, error) -> {
                        if (error != null) {
                            log.error("*************Error while sending create product event: {}*************", error.getMessage());
                        } else {
                            log.info("*************Created product event: {}*************", result.getRecordMetadata());
                        }
                    });
                    log.info("*************Returning product ID*************");

                    return productCreateEvent.getProductId();
                })
                .orElse("");
    }
}
