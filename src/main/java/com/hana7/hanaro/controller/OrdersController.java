package com.hana7.hanaro.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.service.OrdersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
	private final OrdersService ordersService;

	@PostMapping("/{userId}")
	public ResponseEntity<?> makeOrder(@PathVariable Long userId) {
		ordersService.makeOrders(userId);
		return ResponseEntity.ok().build();
	}


}
