package com.dipankar.ops.dto.request;

import jakarta.validation.constraints.NotNull;

public record PaymentRequest(

        @NotNull
        Long orderId
) {}