package com.sparta.msa_eaxm.product.service;

import com.sparta.msa_eaxm.product.domain.Product;
import com.sparta.msa_eaxm.product.domain.ProductDto;
import com.sparta.msa_eaxm.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CachePut(cacheNames = "productCache", key = "'allProducts'")
    public ProductDto createProduct(ProductDto productDto) {
        return ProductDto.fromEntity(productRepository.save(Product.builder()
                .name(productDto.getName())
                .supplyPrice(productDto.getSupply_price())
                .build()));
    }

    @Cacheable(cacheNames = "productCache", key = "'allProducts'")
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDto::fromEntity);
    }

    public ProductDto getProduct(Long productId) {
        return productRepository.findByProductId(productId).map(ProductDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
