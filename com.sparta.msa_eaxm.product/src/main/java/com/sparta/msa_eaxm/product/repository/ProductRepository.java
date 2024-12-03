package com.sparta.msa_eaxm.product.repository;

import com.sparta.msa_eaxm.product.domain.Product;
import com.sparta.msa_eaxm.product.domain.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByProductId(Long productId);
}
