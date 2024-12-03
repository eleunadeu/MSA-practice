package com.sparta.msa_eaxm.product.controller;

import com.sparta.msa_eaxm.product.domain.ProductDto;
import com.sparta.msa_eaxm.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }
}
