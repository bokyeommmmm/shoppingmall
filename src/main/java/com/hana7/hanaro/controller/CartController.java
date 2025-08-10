package com.hana7.hanaro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.dto.CartRequestDTO;
import com.hana7.hanaro.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	@PostMapping("")
	public ResponseEntity<?> addCartItem(@RequestBody CartRequestDTO cartRequestDTO) {
		cartService.addItemToCart(cartRequestDTO);
		return ResponseEntity.ok().build();
	}
}
