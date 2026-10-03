package com.dipankar.ops.service.impl;

import com.dipankar.ops.dto.request.ProductRequest;
import com.dipankar.ops.dto.responce.ProductResponse;
import com.dipankar.ops.entity.Product;
import com.dipankar.ops.repository.ProductRepository;
import com.dipankar.ops.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

   private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .stock((productRequest.stock())).build();

        Product saved = productRepository.save(product);

        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getPrice(),
                saved.getStock()
        );
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()
                ))
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product updated = productRepository.save(product);

        return new ProductResponse(
                updated.getId(),
                updated.getName(),
                updated.getPrice(),
                updated.getStock()
        );
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }
}
