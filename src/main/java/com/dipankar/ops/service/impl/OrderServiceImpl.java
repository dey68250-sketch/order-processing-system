package com.dipankar.ops.service.impl;

import com.dipankar.ops.dto.request.OrderRequest;
import com.dipankar.ops.dto.responce.OrderResponse;
import com.dipankar.ops.entity.Order;
import com.dipankar.ops.repository.OrderReposittory;
import com.dipankar.ops.service.OrderService;
import com.dipankar.ops.service.OrderTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderReposittory orderRepository;
    private final OrderTransactionService orderTransactionService;

    @Override
    public OrderResponse createOrder(
            OrderRequest orderRequest,
            String idempotencyKey) {

        // Step 1: Check whether this request was already processed
        Order existingOrder = orderRepository
                .findByIdempotencyKey(idempotencyKey)
                .orElse(null);

        if (existingOrder != null) {
            return toResponse(existingOrder);
        }

        try {

            // Step 2: Try to create the order
            return orderTransactionService.createNewOrder(
                    orderRequest,
                    idempotencyKey
            );

        } catch (DataIntegrityViolationException e) {

            // Step 3:
            // Another concurrent request created the order
            // with the same idempotency key.

            Order concurrentOrder = orderRepository
                    .findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Order creation conflict. Please retry."
                            )
                    );

            // Step 4: Return the order created by the other request
            return toResponse(concurrentOrder);
        }
    }

    private OrderResponse toResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderedAt()
        );
    }
}