package com.sparta.msa_eaxm.order.service;

import com.sparta.msa_eaxm.order.controller.ProductClient;
import com.sparta.msa_eaxm.order.domain.Order;
import com.sparta.msa_eaxm.order.domain.OrderDto;
import com.sparta.msa_eaxm.order.domain.ProductDto;
import com.sparta.msa_eaxm.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    private Long getProductId(Long productId) {
        return productClient.getProductId(productId).getProductId();
    }

    public OrderDto createOrder(OrderDto orderDto) {
        List<Long> productIds = orderDto.getProductIds()
                .stream().map(productId -> {
                    ProductDto product = productClient.getProductId(productId);
                    if (product.getProductId() == null) {
                        // 상품 API 호출 실패 시 fallback 처리
                        throw new IllegalArgumentException("잠시 후에 주문 추가를 요청 해주세요.");
                    }
                    return product.getProductId();
                })
                .collect(Collectors.toList());

        if (productIds.size() != orderDto.getProductIds().size()) {
            throw new IllegalArgumentException("주문의 상품 ID에 해당하는 상품이 없습니다.");
        }

        Order order = Order.builder()
                .productIds(productIds)
                .build();

        Order savedOrder = orderRepository.save(order);

        return OrderDto.fromEntity(savedOrder);
    }

    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findByOrderId(orderId).map(OrderDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public OrderDto addProduct(Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        orderDto.getProductIds().forEach(productId -> {
            ProductDto product = productClient.getProductId(productId);
            if (product.getProductId() == null) {
                throw new IllegalArgumentException("잠시 후에 주문 추가를 요청 해주세요.");
            }
        });

        order.addProductId(orderDto.getProductIds());
        orderRepository.save(order);

        return OrderDto.fromEntity(order);
    }
}
