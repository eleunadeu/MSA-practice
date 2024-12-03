package com.sparta.msa_eaxm.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long productId;
    private String name;
    private Integer supplyPrice;

    public ProductDto(Object o, String s) {
        productId = null;
        name = s;
        supplyPrice = null;
    }
}
