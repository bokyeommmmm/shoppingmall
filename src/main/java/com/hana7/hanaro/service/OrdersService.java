package com.hana7.hanaro.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hana7.hanaro.dto.OrderResponseDTO;
import com.hana7.hanaro.exception.NotFound.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.OrderItem;
import com.hana7.hanaro.entity.Orders;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.enums.ORDERSTATUS;
import com.hana7.hanaro.repository.CartItemRepository;
import com.hana7.hanaro.repository.OrderItemRepository;
import com.hana7.hanaro.repository.OrdersRepository;
import com.hana7.hanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //파이널에선 항상 .
@Transactional(readOnly = true)
public class OrdersService {
	private final OrdersRepository ordersRepository;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderItemRepository orderItemRepository;

	@Transactional
	public void makeOrders(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
		Cart cart = user.getCart();

		List<CartItem> items = cartItemRepository.findByCart(cart);

		int totalPrice = items.stream().mapToInt(item -> {
			int price = item.getItem().getPrice();
			int amount = item.getAmount();
			return  price * amount;
		}).sum();

		Orders order = Orders.builder()
			.user(user)
			.status(ORDERSTATUS.PAID)
			.totalPrice(totalPrice)
			.build();

		Orders saved = ordersRepository.save(order);

		List<OrderItem> orderItems = items.stream()
			.map(cartItem -> OrderItem.builder()
				.order(saved) // 'orders' 필드명을 'order'로 수정
				.item(cartItem.getItem())
				.amount(cartItem.getAmount())
				.build())
			.collect(Collectors.toList());

		orderItemRepository.saveAll(orderItems); // saveAll을 사용하여 성능 개선
	}

	public Page<OrderResponseDTO> getOrderHistory(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
		Page<Orders> orders = ordersRepository.findByUser(user, pageable);
		return orders.map(OrderResponseDTO::fromEntity);
	}
}
