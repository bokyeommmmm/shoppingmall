package com.hana7.hanaro.dto;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.Item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemRequestDTO {
	private Long itemId;
	private int amount;

	public CartItem toEntity(Cart cart , Item item){
		return CartItem.builder()
			.cart(cart)
			.item(item)
			.amount(amount)
			.build();
	}
}
