package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.OrderResponseDTO;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.service.OrdersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
	private final OrdersService ordersService;

	@PostMapping("/users/{userId}/orders")
	public ResponseEntity<Void> makeOrder(@PathVariable("userId") Long userId) {
		ordersService.makeOrders(userId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<Page<OrderResponseDTO>> getUserOrders(
		@PathVariable("userId") Long userId,
		@ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
		Page<OrderResponseDTO> orderHistory = ordersService.getOrderHistory(userId, pageable);
		return ResponseEntity.ok(orderHistory);
	}

	@GetMapping("/orders/me")
	public ResponseEntity<Page<OrderResponseDTO>> getMyOrders(
		@ParameterObject @PageableDefault(size=10,page=0) Pageable pageable,
			Authentication authentication){
		return ResponseEntity.ok(ordersService.getMyOrders(authentication.getName(), pageable));
	}

	@GetMapping("/date")
	public ResponseEntity<Page<OrderResponseDTO>> getOrdersByDate(
		@ParameterObject @PageableDefault(size=10,page=0) Pageable pageable,
		@RequestParam String start, @RequestParam String end){

		return ResponseEntity.ok(ordersService.getOrdersByDate(start, end, pageable));

	}
	@GetMapping("/user")
	public ResponseEntity<Page<OrderResponseDTO>> getOrdersUser(
		@ParameterObject @PageableDefault(size=10,page=0) Pageable pageable,
		@RequestParam String userName){

		return ResponseEntity.ok(ordersService.getOrdersByUser(userName, pageable));

	}


}
