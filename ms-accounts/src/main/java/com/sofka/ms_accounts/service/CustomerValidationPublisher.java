package com.sofka.ms_accounts.service;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

@Service
public class CustomerValidationPublisher {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CustomerValidationPublisher.class);


    public CustomerValidationPublisher(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public CompletableFuture<Boolean> validateCustomer(UUID customerId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            String customerIdString = customerId.toString();
            logger.info("📩 Enviando validación de cliente: {}", customerIdString);
            logger.info("🔹 Enviando mensaje a la cola customer.validation.request con ID: {}", customerIdString);
            rabbitTemplate.convertAndSend("customer.validation.request", customerIdString);

            // Esperar la respuesta
            new Thread(() -> {
                try {
                    String response = (String) rabbitTemplate.receiveAndConvert("customer.validation.response");
                    if (response != null) {
                        String[] parts = response.split(":");
                        UUID receivedCustomerId = UUID.fromString(parts[0]);
                        boolean exists = Boolean.parseBoolean(parts[1]);

                        logger.info("🔹 Respuesta recibida para el cliente {}: {}", receivedCustomerId, exists);

                        if (receivedCustomerId.equals(customerId)) {
                            future.complete(exists);
                        } else {
                            future.completeExceptionally(new RuntimeException("ID de cliente no coincide"));
                        }
                    } else {
                        future.completeExceptionally(new RuntimeException("No se recibió respuesta"));
                    }
                } catch (Exception e) {
                    logger.error("❌ Error al recibir la respuesta: {}", e.getMessage(), e);
                    future.completeExceptionally(e);
                }
            }).start();

        } catch (Exception e) {
            logger.error("❌ Error al enviar mensaje a RabbitMQ: ", e);
            future.completeExceptionally(e);
        }

        return future;
    }
}
