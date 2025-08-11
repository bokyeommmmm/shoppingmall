package com.hana7.hanaro.dto;

import com.hana7.hanaro.entity.OrderItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponseDTO {
    private Long itemId;
    private String itemName;
    private int amount;
    private int price; // 주문 당시의 가격

    public static OrderItemResponseDTO fromEntity(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
            .itemId(orderItem.getItem().getId())
            .itemName(orderItem.getItem().getItemName())
            .amount(orderItem.getAmount())
            .price(orderItem.getItem().getPrice()) // 예시로 현재 상품 가격을 사용, 별도 주문 가격 필드가 있다면 그것을 사용
            .build();
    }
}