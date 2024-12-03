package com.sparta.msa_eaxm.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private Integer supplyPrice;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSupplyPrice(Integer supplyPrice) {
        this.supplyPrice = supplyPrice;
    }
}
