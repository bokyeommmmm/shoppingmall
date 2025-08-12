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

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.hana7.hanaro.repository.OrdersCustomRepository;
import com.hana7.hanaro.repository.OrdersRepository;
import com.hana7.hanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor //파이널에선 항상 .
@Transactional
@Log4j2
public class OrdersService {
	private final OrdersRepository ordersRepository;
	private final OrdersCustomRepository ordersCustomRepository;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemRepository itemRepository;
	private final JobLauncher jobLauncher;
	private final Job csvJob;

	@Transactional
	public void makeOrders(Long userId) {
		log.info("[makeOrders] 시작 - userId={}", userId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> {
				log.warn("[makeOrders] 사용자 없음 - userId={}", userId);
				return new UserNotFoundException();
			});
		log.info("[makeOrders] 사용자 조회 성공 - userId={}", userId);

		Cart cart = user.getCart();
		log.info("[makeOrders] 장바구니 ID={}", cart.getId());

		List<CartItem> items = cartItemRepository.findByCart(cart);
		log.info("[makeOrders] 장바구니 상품 수 = {}", items.size());

		items.forEach(item -> {
			if(item.getItem().getQuantity() < item.getAmount()){
				log.warn("[makeOrders] 재고 부족 - itemId={}, 재고={}, 요청수량={}",
					item.getItem().getId(), item.getItem().getQuantity(), item.getAmount());
				throw new OrderBadRequestException();
			}
		});

		int totalPrice = items.stream().mapToInt(item -> {
			int price = item.getItem().getPrice();
			int amount = item.getAmount();
			return  price * amount;
		}).sum();
		log.info("[makeOrders] 총 주문 금액 계산 완료 - totalPrice={}", totalPrice);

		Orders order = Orders.builder()
			.user(user)
			.orderStatus(ORDERSTATUS.PAID)
			.totalPrice(totalPrice)
			.build();

		Orders saved = ordersRepository.save(order);
		log.info("[makeOrders] 주문 저장 완료 - orderId={}", saved.getId());

		List<OrderItem> orderItems = items.stream()
			.map(cartItem -> {
				Item item = itemRepository.findById(cartItem.getItem().getId())
					.orElseThrow(() -> {
						log.warn("[makeOrders] 상품 없음 - itemId={}", cartItem.getItem().getId());
						return new ItemNotFoundException();
					});
				Item updated = item.toBuilder()
					.quantity(item.getQuantity() - cartItem.getAmount())
					.build();
				itemRepository.save(updated);
				log.info("[makeOrders] 상품 재고 차감 - itemId={}, 남은재고={}",
					item.getId(), updated.getQuantity());

				return OrderItem.builder()
					.order(saved)
					.item(cartItem.getItem())
					.amount(cartItem.getAmount())
					.build();
			})
			.collect(Collectors.toList());

		orderItemRepository.saveAll(orderItems);
		log.info("[makeOrders] 주문 상세 저장 완료 - count={}", orderItems.size());

		cartItemRepository.deleteAll(items);
		log.info("[makeOrders] 장바구니 비움 완료");

		log.info("[makeOrders] 종료 - orderId={}", saved.getId());
	}

	public Page<OrderResponseDTO> getOrderHistory(Long userId, Pageable pageable) {
		log.info("[getOrderHistory] 시작 - userId={}", userId);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> {
				log.warn("[getOrderHistory] 사용자 없음 - userId={}", userId);
				return new UserNotFoundException();
			});

		Page<Orders> orders = ordersRepository.findByUser(user, pageable);
		log.info("[getOrderHistory] 조회된 주문 수 = {}", orders.getTotalElements());
		return orders.map(this::toDto);
	}

	public Page<OrderResponseDTO> getMyOrders(String email, Pageable pageable){
		log.info("[getMyOrders] 시작 - email={}", email);
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> {
				log.warn("[getMyOrders] 사용자 없음 - email={}", email);
				return new UserNotFoundException();
			});

		Page<Orders> orders = ordersRepository.findByUser(user, pageable);
		log.info("[getMyOrders] 조회된 주문 수 = {}", orders.getTotalElements());
		return orders.map(this::toDto);
	}

	public Page<OrderResponseDTO> getOrdersByDate(String start, String end, Pageable pageable){
		log.info("[getOrdersByDate] 시작 - start={}, end={}", start, end);
		LocalDateTime startDateTime = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE).atTime(23, 59, 59);

		Page<Orders> orders = ordersRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);

		if(orders.getTotalElements() == 0){
			log.warn("[getOrdersByDate] 해당 기간 주문 없음");
			throw new OrderNotFoundException();
		}

		log.info("[getOrdersByDate] 조회된 주문 수 = {}", orders.getTotalElements());
		return orders.map(this::toDto);
	}

	public Page<OrderResponseDTO> getOrdersByUser(String userName, Pageable pageable){
		log.info("[getOrdersByUser] 시작 - userName={}", userName);
		User user = userRepository.findByUserName(userName)
			.orElseThrow(() -> {
				log.warn("[getOrdersByUser] 사용자 없음 - userName={}", userName);
				return new UserNotFoundException();
			});
		Page<Orders> orders = ordersRepository.findByUser(user, pageable);

		if(orders.getTotalElements() == 0){
			log.warn("[getOrdersByUser] 해당 사용자 주문 없음");
			throw new OrderNotFoundException();
		}

		log.info("[getOrdersByUser] 조회된 주문 수 = {}", orders.getTotalElements());
		return orders.map(this::toDto);
	}

	public BatchStatus runBatch() throws Exception {
		log.info("[runBatch] 시작");
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("time", System.currentTimeMillis())
			.toJobParameters();

		BatchStatus status = jobLauncher.run(csvJob, jobParameters).getStatus();
		log.info("[runBatch] 종료 - status={}", status);
		return status;
	}

	@Scheduled(cron = "0 */5 * * * *")
	public void updateStateBatch() throws Exception {
		log.info("[updateStateBatch] 시작");
		ORDERSTATUS state = ORDERSTATUS.PAID;
		while (state != ORDERSTATUS.DELIVERED) {
			LocalDateTime now = LocalDateTime.now();
			log.info("[updateStateBatch] {} -> {} 진행 중", state, state.getNextState());

			long affectedRowCount = ordersCustomRepository.updateStateBatch(
				state, state.getNextState(), now.minusMinutes(state.stateInterval())
			);
			log.info("[updateStateBatch] 변경된 행 수 = {}", affectedRowCount);

			state = state.getNextState();
		}
		log.info("[updateStateBatch] 종료");
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
