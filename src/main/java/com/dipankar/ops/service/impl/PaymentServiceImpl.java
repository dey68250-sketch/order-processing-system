package com.dipankar.ops.service.impl;

import com.dipankar.ops.dto.request.PaymentRequest;
import com.dipankar.ops.dto.responce.PaymentResponse;
import com.dipankar.ops.entity.Order;
import com.dipankar.ops.entity.OrderStatus;
import com.dipankar.ops.entity.PaymentStatus;
import com.dipankar.ops.repository.OrderReposittory;
import com.dipankar.ops.service.PaymentService;
import com.dipankar.ops.service.PaymentTransactionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderReposittory orderRepository;
    private final PaymentTransactionService paymentTransactionService;


    @CircuitBreaker(
            name = "paymentGateway",
            fallbackMethod = "paymentFallback"
    )
    @Override
    public PaymentResponse makePayment(PaymentRequest request) {

        return paymentTransactionService.processPayment(request);
    }


    public PaymentResponse paymentFallback(
            PaymentRequest request,
            Exception ex) {

        System.out.println("Payment failed: " + ex.getMessage());

        return paymentTransactionService.cancelPayment(request);
    }
}