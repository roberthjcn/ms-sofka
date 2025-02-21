package com.sofka.ms_accounts.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerValidationConsumer {
    private final Map<UUID, Boolean> validationResponses = new ConcurrentHashMap<>();
    @RabbitListener(queues = "customer.validation.response")
    public void receiveValidation(String message) {
        String[] parts = message.split(":");
        UUID customerId = UUID.fromString(parts[0]);
        boolean exists = Boolean.parseBoolean(parts[1]);

        validationResponses.put(customerId, exists);

        if (!exists) {
            System.out.println("✅ Cliente " + customerId + " existe.");
        } else {
            System.out.println("❌ Cliente " + customerId + " NO existe.");
        }
    }

    public Boolean getValidationResponse(UUID customerId) {
        return validationResponses.get(customerId);
    }
}
