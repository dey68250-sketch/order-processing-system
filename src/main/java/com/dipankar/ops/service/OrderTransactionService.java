package com.dipankar.ops.service;

import com.dipankar.ops.dto.request.OrderRequest;
import com.dipankar.ops.dto.responce.OrderResponse;
import com.dipankar.ops.entity.Order;
import com.dipankar.ops.entity.OrderStatus;
import com.dipankar.ops.entity.Product;
import com.dipankar.ops.repository.OrderReposittory;
import com.dipankar.ops.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final OrderReposittory orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createNewOrder(
            OrderRequest orderRequest,
            String idempotencyKey) {

        Product product = productRepository
                .findByIdForUpdate(orderRequest.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < orderRequest.quantity()) {
            throw new RuntimeException("Out of stock");
        }

        BigDecimal total = product.getPrice()
                .multiply(BigDecimal.valueOf(orderRequest.quantity()));

        Order order = Order.builder()
                .product(product)
                .quantity(orderRequest.quantity())
                .totalAmount(total)
                .status(OrderStatus.PENDING)
                .orderedAt(LocalDateTime.now())
                .idempotencyKey(idempotencyKey)
                .build();

        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                product.getId(),
                product.getName(),
                saved.getQuantity(),
                saved.getTotalAmount(),
                saved.getStatus(),
                saved.getOrderedAt()
        );
    }
}