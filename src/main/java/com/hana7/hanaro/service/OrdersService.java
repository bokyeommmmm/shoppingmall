package com.hana7.hanaro.service;

import java.util.List;

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
@Transactional
public class OrdersService {
	private final OrdersRepository ordersRepository;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderItemRepository orderItemRepository;

	public void makeOrders(Long userId){
		User user = userRepository.findById(userId).orElse(null);
		// if(user == null){}
		assert user != null;
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

		items.forEach(cartItem -> {
			OrderItem orderItem = OrderItem.builder()
				.orders(saved)
				.item(cartItem.getItem())
				.amount(cartItem.getAmount())
				.build();
			orderItemRepository.save(orderItem);
		});
	}
}
