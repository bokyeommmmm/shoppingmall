package com.hana7.hanaro.dto;

import com.hana7.hanaro.entity.Orders;
import com.hana7.hanaro.enums.ORDERSTATUS;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private ORDERSTATUS orderStatus;
    private int totalPrice;
    private List<OrderItemResponseDTO> orderItems;

    public static OrderResponseDTO fromEntity(Orders order) {
        return OrderResponseDTO.builder()
            .orderId(order.getId())
            .orderDate(order.getCreatedAt()) // BaseEntity의 생성일자를 주문일자로 사용
            .orderStatus(order.getStatus())
            .totalPrice(order.getTotalPrice())
            .orderItems(order.getOrderItems().stream().map(OrderItemResponseDTO::fromEntity).collect(Collectors.toList()))
            .build();
    }
}