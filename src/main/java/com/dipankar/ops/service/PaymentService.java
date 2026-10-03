package com.dipankar.ops.service;

import com.dipankar.ops.dto.request.PaymentRequest;
import com.dipankar.ops.dto.responce.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);
}