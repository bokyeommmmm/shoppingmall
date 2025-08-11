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
    private String orderDate;
    private String orderStatus;
    private int totalPrice;
    private List<OrderItemResponseDTO> orderItems;


}
