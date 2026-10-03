package com.dipankar.ops.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class FakePaymentGateway {

    private final double failureRate;

    public FakePaymentGateway(
            @Value("${payment.gateway.failure-rate:0.3}") double failureRate) {
        this.failureRate = failureRate;
    }

    public void charge() {
        System.out.println("Gateway called");

        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            throw new RuntimeException("Payment Gateway Down");
        }

        System.out.println("Gateway charge successful");
    }
}