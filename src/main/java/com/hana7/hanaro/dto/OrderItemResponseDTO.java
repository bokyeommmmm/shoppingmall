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
    private int totalPrice;
}
