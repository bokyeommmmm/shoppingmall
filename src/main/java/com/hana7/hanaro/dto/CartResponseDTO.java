package com.hana7.hanaro.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CartResponseDTO {
	private Long id;
	private List<CartItemResponseDTO> items;
}
