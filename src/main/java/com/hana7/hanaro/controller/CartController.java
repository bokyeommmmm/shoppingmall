package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.CartItemUpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.dto.CartRequestDTO;
import com.hana7.hanaro.dto.CartResponseDTO;
import com.hana7.hanaro.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	@PostMapping("")
	public ResponseEntity<?> addCartItem(@RequestBody CartRequestDTO cartRequestDTO ,Authentication authentication) {
		cartService.addItemToCart(cartRequestDTO,authentication.getName());
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/items/{cartItemId}")
	public ResponseEntity<?> updateCartItemAmount(@PathVariable Long cartItemId, @RequestBody CartItemUpdateRequestDTO dto) {
		cartService.updateCartItemAmount(cartItemId, dto.getAmount());
		return ResponseEntity.ok().build();
	}


	@DeleteMapping("/items/{cartItemId}")
	public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
		cartService.deleteCartItem(cartItemId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("")
	public ResponseEntity<CartResponseDTO> getCartItems (Authentication authentication){
		String email = authentication.getName();//로그인을 이메일로 하니까 ~~

		return ResponseEntity.ok(cartService.getCart(email));
	}
}
