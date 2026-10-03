package com.dipankar.ops.service;

import com.dipankar.ops.dto.request.OrderRequest;
import com.dipankar.ops.dto.responce.OrderResponse;
import org.jspecify.annotations.Nullable;

public interface OrderService {
     OrderResponse createOrder(OrderRequest orderRequest,String idempotencyKey);
}
