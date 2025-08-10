package com.hana7.hanaro.dto;

import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartRequestDTO {

	private Long userId;

	private List<CartItemRequestDTO> cartItems;


}
