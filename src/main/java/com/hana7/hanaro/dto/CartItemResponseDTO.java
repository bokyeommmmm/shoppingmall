package com.hana7.hanaro.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponseDTO {
	private Long id;
	private String itemName;
	private int amount;
	private int price;

}
