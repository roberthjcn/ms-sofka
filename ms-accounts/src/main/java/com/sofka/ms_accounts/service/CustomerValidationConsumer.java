package com.sofka.ms_accounts.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerValidationConsumer {
    @RabbitListener(queues = "customer.validation.response")
    public void receiveValidation(String message) {
        String[] parts = message.split(":");
        UUID customerId = UUID.fromString(parts[0]);
        boolean exists = Boolean.parseBoolean(parts[1]);

        if (exists) {
            System.out.println("✅ Cliente " + customerId + " existe.");
        } else {
            System.out.println("❌ Cliente " + customerId + " NO existe.");
        }
    }
}
