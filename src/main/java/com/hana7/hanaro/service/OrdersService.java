package com.hana7.hanaro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.hana7.hanaro.dto.OrderItemResponseDTO;
import com.hana7.hanaro.dto.OrderResponseDTO;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.exception.BadRequest.OrderBadRequestException;
import com.hana7.hanaro.exception.NotFound.ItemNotFoundException;
import com.hana7.hanaro.exception.NotFound.OrderNotFoundException;
import com.hana7.hanaro.exception.NotFound.UserNotFoundException;

import org.springframework.cglib.core.Local;
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
import com.hana7.hanaro.repository.ItemRepository;
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
	private final ItemRepository itemRepository;

	@Transactional
	public void makeOrders(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
		Cart cart = user.getCart();

		List<CartItem> items = cartItemRepository.findByCart(cart);

		items.forEach(item -> {
			if(item.getItem().getQuantity() <item.getAmount()){
				throw new OrderBadRequestException();
			}
		});

		int totalPrice = items.stream().mapToInt(item -> {
			int price = item.getItem().getPrice();
			int amount = item.getAmount();
			return  price * amount;
		}).sum();

		Orders order = Orders.builder()
			.user(user)
			.orderStatus(ORDERSTATUS.PAID)
			.totalPrice(totalPrice)
			.build();

		Orders saved = ordersRepository.save(order);



		List<OrderItem> orderItems = items.stream()
			.map(cartItem -> {
				Item item = itemRepository.findById(cartItem.getItem().getId()).orElseThrow(ItemNotFoundException::new);
				Item updated = item.toBuilder().quantity(item.getQuantity()-cartItem.getAmount()).build();
				itemRepository.save(updated);

				return OrderItem.builder()
				.order(saved)
				.item(cartItem.getItem())
				.amount(cartItem.getAmount())
				.build();
			})
			.collect(Collectors.toList());

		orderItemRepository.saveAll(orderItems);

		// 주문이 완료-> 장바구니의 상품들을 비움
		cartItemRepository.deleteAll(items);
	}

	public Page<OrderResponseDTO> getOrderHistory(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
		Page<Orders> orders = ordersRepository.findByUser(user, pageable);
		return orders.map(this::toDto);
	}

	public Page<OrderResponseDTO> getMyOrders(String email, Pageable pageable){
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		Page<Orders> orders = ordersRepository.findByUser(user, pageable);

		return orders.map(this::toDto);
	}

	//주문내역 조회 (관리자 기능)
	public Page<OrderResponseDTO> getOrdersByDate(String start, String end, Pageable pageable){
		LocalDateTime startDateTime = LocalDate.parse(start,DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(end,DateTimeFormatter.ISO_LOCAL_DATE).atTime(23,59,59);
		Page<Orders> orders = ordersRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);

		if(orders.getTotalElements()==0){
			throw new OrderNotFoundException();
		}

		return orders.map(this::toDto);

	}
	public Page<OrderResponseDTO> getOrdersByUser(String userName, Pageable pageable){
		User user = userRepository.findByUserName(userName).orElseThrow(UserNotFoundException::new);
		Page<Orders> orders = ordersRepository.findByUser(user, pageable);

		if(orders.getTotalElements()==0){
			throw new OrderNotFoundException();
		}

		return orders.map(this::toDto);

	}
	private OrderResponseDTO toDto(Orders order){
		//주문에 담긴 주문 상품 정보 가져옴
		List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
		//주문 상품 객체를 DTO로 전환
		List<OrderItemResponseDTO> itemDtos = orderItems.stream().map(orderItem ->
			OrderItemResponseDTO.builder()
				.itemId(orderItem.getId())
				.itemName(orderItem.getItem().getItemName())
				.amount(orderItem.getAmount())
				.totalPrice(orderItem.getAmount()*orderItem.getItem().getPrice())
				.build()).toList();

		return OrderResponseDTO.builder()
			.orderId(order.getId())
			.orderDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
			.orderStatus(order.getOrderStatus().getDescription())
			.totalPrice(order.getTotalPrice())
			.orderItems(itemDtos)
			.build();
	}
}
