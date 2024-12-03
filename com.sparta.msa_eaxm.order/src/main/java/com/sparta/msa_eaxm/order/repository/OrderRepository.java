package com.sparta.msa_eaxm.order.repository;

import com.sparta.msa_eaxm.order.domain.Order;
import com.sparta.msa_eaxm.order.domain.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(Long orderId);
}
