package com.dipankar.ops.controller;


import com.dipankar.ops.dto.request.PaymentRequest;
import com.dipankar.ops.dto.responce.PaymentResponse;
import com.dipankar.ops.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.makePayment(request));
    }
}