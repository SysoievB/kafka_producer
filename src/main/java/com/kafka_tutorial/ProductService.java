package com.kafka_tutorial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
                    try {
                        log.info("*************Before creating product event: {}*************", productCreateEvent);
                        SendResult<String, ProductCreateEvent> result =
                                kafkaTemplate.send(
                                        "product-created-event-topic",
                                        productCreateEvent.getProductId(),
                                        productCreateEvent
                                ).get();
                        log.info("*************Partition: {}*************", result.getRecordMetadata().partition());
                        log.info("*************Topic: {}*************", result.getRecordMetadata().topic());
                        log.info("*************Offset: {}*************", result.getRecordMetadata().offset());
                        log.info("*************Returning product ID*************");
                        return productCreateEvent.getProductId();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("************* Error while sending create product event: {} *************", e.getMessage());

                        throw new RuntimeException("Kafka send failed: " + e.getMessage(), e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Failed to create product"));
    }
}
