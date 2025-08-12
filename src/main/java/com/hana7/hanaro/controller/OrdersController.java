package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.OrderResponseDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hana7.hanaro.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Log4j2
public class OrdersController {
	private final OrdersService ordersService;

	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "장바구니 아이템 주문")
	@PostMapping("/users/{userId}/orders")
	public ResponseEntity<Void> makeOrder(@PathVariable("userId") Long userId) {
		log.info("[makeOrder] 시작 - userId={}", userId);

		ordersService.makeOrders(userId);

		log.info("[makeOrder] 종료 - userId={}", userId);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "주문 내역 조회 (관리자)")
	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<Page<OrderResponseDTO>> getUserOrders(
		@PathVariable("userId") Long userId,
		@ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

		log.info("[getUserOrders] 시작 - userId={}, page={}, size={}",
			userId, pageable.getPageNumber(), pageable.getPageSize());

		Page<OrderResponseDTO> orderHistory = ordersService.getOrderHistory(userId, pageable);

		log.info("[getUserOrders] 종료 - userId={}, totalOrders={}", userId, orderHistory.getTotalElements());
		return ResponseEntity.ok(orderHistory);
	}

	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "내 주문내역 조회(유저)")
	@GetMapping("/orders/me")
	public ResponseEntity<Page<OrderResponseDTO>> getMyOrders(
		@ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable,
		Authentication authentication) {

		log.info("[getMyOrders] 시작 - email={}, page={}, size={}",
			authentication.getName(), pageable.getPageNumber(), pageable.getPageSize());

		Page<OrderResponseDTO> myOrders = ordersService.getMyOrders(authentication.getName(), pageable);

		log.info("[getMyOrders] 종료 - email={}, totalOrders={}",
			authentication.getName(), myOrders.getTotalElements());
		return ResponseEntity.ok(myOrders);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "날짜로 주문내역 조회 (관리자)")
	@GetMapping("/date")
	public ResponseEntity<Page<OrderResponseDTO>> getOrdersByDate(
		@ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam String start,
		@RequestParam String end) {

		log.info("[getOrdersByDate] 시작 - start={}, end={}, page={}, size={}",
			start, end, pageable.getPageNumber(), pageable.getPageSize());

		Page<OrderResponseDTO> orders = ordersService.getOrdersByDate(start, end, pageable);

		log.info("[getOrdersByDate] 종료 - start={}, end={}, totalOrders={}",
			start, end, orders.getTotalElements());
		return ResponseEntity.ok(orders);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "사용자로 주문내역 조회 (관리자)")
	@GetMapping("/user")
	public ResponseEntity<Page<OrderResponseDTO>> getOrdersUser(
		@ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam String userName) {

		log.info("[getOrdersUser] 시작 - userName={}, page={}, size={}",
			userName, pageable.getPageNumber(), pageable.getPageSize());

		Page<OrderResponseDTO> orders = ordersService.getOrdersByUser(userName, pageable);

		log.info("[getOrdersUser] 종료 - userName={}, totalOrders={}",
			userName, orders.getTotalElements());
		return ResponseEntity.ok(orders);
	}
}
