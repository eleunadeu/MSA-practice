package com.sparta.msa_eaxm.order.controller;

import com.sparta.msa_eaxm.order.domain.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/product/{productId}")
    ProductDto getProductId(@PathVariable("productId") Long productId);
}
