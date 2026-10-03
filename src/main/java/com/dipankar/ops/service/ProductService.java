package com.dipankar.ops.service;

import com.dipankar.ops.dto.request.ProductRequest;
import com.dipankar.ops.dto.responce.ProductResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    @Nullable List<ProductResponse> getAllProducts();

    @Nullable ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
