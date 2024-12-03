package com.sparta.msa_eaxm.order.controller;

import com.sparta.msa_eaxm.order.domain.ProductDto;

public class ProductClientFallback implements ProductClient {
    @Override
    public ProductDto getProductId(Long productId) {
        return new ProductDto(null, "잠시 후에 다시 요청해 주세요.");
    }
}
