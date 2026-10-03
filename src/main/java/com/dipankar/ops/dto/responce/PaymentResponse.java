package com.dipankar.ops.dto.responce;

import com.dipankar.ops.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long paymentId,

        Long orderId,

        BigDecimal amount,

        PaymentStatus status,

        LocalDateTime paidAt
) {}
