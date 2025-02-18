package com.sofka.ms_accounts.service;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.slf4j.Logger;

@Service
public class CustomerValidationPublisher {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CustomerValidationPublisher.class);


    public CustomerValidationPublisher(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void validateCustomer(UUID customerId) {
        try {
            String customerIdString = customerId.toString();
            logger.info("📩 Enviando validación de cliente: {}", customerIdString);
            logger.info("🔹 Enviando mensaje a la cola customer.validation.request con ID: {}", customerIdString);
            rabbitTemplate.convertAndSend("customer.validation.request", customerIdString);

        } catch (Exception e) {
            logger.error("❌ Error al enviar mensaje a RabbitMQ: ", e);
        }
    }
}
