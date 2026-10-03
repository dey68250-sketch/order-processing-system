package com.dipankar.ops.repository;

import com.dipankar.ops.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderReposittory extends JpaRepository<Order, Long> {
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
