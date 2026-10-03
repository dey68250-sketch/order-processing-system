package com.dipankar.ops.dto.responce;

import com.dipankar.ops.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(

        Long orderId,

        Long productId,

        String productName,

        Integer quantity,

        BigDecimal totalAmount,

        OrderStatus status,

        LocalDateTime orderedAt
) {}