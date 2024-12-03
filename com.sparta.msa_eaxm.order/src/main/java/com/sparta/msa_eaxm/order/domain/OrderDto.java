package com.sparta.msa_eaxm.order.domain;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long orderId;
    private List<Long> productIds;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .productIds(order.getProductIds())
                .build();
    }
}
