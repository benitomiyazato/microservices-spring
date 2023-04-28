package com.benitomiyazato.productservice.service;

import com.benitomiyazato.productservice.dto.ProductRequest;
import com.benitomiyazato.productservice.dto.ProductResponse;
import com.benitomiyazato.productservice.model.Product;
import com.benitomiyazato.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product productToSave = new Product();
        BeanUtils.copyProperties(productRequest, productToSave);

        productRepository.save(productToSave);
        log.info("Product {} saved", productToSave.getName());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }
}
