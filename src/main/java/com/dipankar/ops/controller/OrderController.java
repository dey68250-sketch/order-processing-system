package com.dipankar.ops.controller;


import com.dipankar.ops.dto.request.OrderRequest;
import com.dipankar.ops.dto.responce.OrderResponse;
import com.dipankar.ops.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
           @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest, idempotencyKey));
    }
}
