package com.dipankar.ops.service;

import com.dipankar.ops.dto.request.PaymentRequest;
import com.dipankar.ops.dto.responce.PaymentResponse;
import com.dipankar.ops.entity.*;
import com.dipankar.ops.repository.OrderReposittory;
import com.dipankar.ops.repository.PaymentRepository;
import com.dipankar.ops.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final OrderReposittory orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final FakePaymentGateway fakePaymentGateway;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(
                    "Order is not payable in its current status: "
                            + order.getStatus()
            );
        }

        Product product = productRepository
                .findByIdForUpdate(order.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("Out of stock - cannot complete payment");
        }

        // External payment call
        fakePaymentGateway.charge();

        // Only after successful payment
        product.setStock(
                product.getStock() - order.getQuantity()
        );

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.CONFIRMED)
                .paidAt(java.time.LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        order.setStatus(OrderStatus.COMPLETED);

        return new PaymentResponse(
                saved.getId(),
                order.getId(),
                saved.getAmount(),
                saved.getStatus(),
                saved.getPaidAt()
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentResponse cancelPayment(PaymentRequest request) {

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        return new PaymentResponse(
                null,
                order.getId(),
                order.getTotalAmount(),
                PaymentStatus.CANCELLED,
                null
        );
    }
}
