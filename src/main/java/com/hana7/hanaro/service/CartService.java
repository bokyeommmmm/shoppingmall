package com.hana7.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana7.hanaro.dto.CartItemRequestDTO;
import com.hana7.hanaro.dto.CartRequestDTO;
import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.exception.BadRequest.CartBadRequestException;
import com.hana7.hanaro.exception.NotFound.CartNotFoundException;
import com.hana7.hanaro.exception.NotFound.ItemNotFoundException;
import com.hana7.hanaro.exception.NotFound.UserNotFoundException;
import com.hana7.hanaro.repository.CartItemRepository;
import com.hana7.hanaro.repository.CartRepository;
import com.hana7.hanaro.repository.ItemRepository;
import com.hana7.hanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //파이널에선 항상 .
@Transactional
public class CartService {
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	private final CartItemRepository cartItemRepository;

	public void addItemToCart(CartRequestDTO cartRequestDTO) {

		User user = userRepository.findById(cartRequestDTO.getUserId()).orElseThrow(UserNotFoundException::new);

		Cart cart = cartRepository.findByUser(user).orElse(null);

		if(cart == null) { //user 가 cart 없다면 만들어주기
			cart = Cart.builder()
				.user(user)
				.build();
			try {
				cartRepository.save(cart);
			} catch (Exception e) {
				throw new CartBadRequestException();
			}
		}

		//stream 인에서 사용할 final 객체 !~~!!!
		Cart finalCart = cart;

		List<CartItem> cartItems = cartRequestDTO.getCartItems().stream()
			.map((cartItem) -> {
				Item item = itemRepository.findById(cartItem.getItemId()).orElseThrow(ItemNotFoundException::new);
				return cartItem.toEntity(finalCart, item);
			}).toList();
		cartItemRepository.saveAll(cartItems);
	}

	//수정
	public void updateCart(CartRequestDTO cartRequestDTO) {
		//user cart 있는지 확인

		User user = userRepository.findById(cartRequestDTO.getUserId()).orElseThrow(UserNotFoundException::new);
		Cart cart = cartRepository.findByUser(user).orElseThrow(CartNotFoundException::new);

		List<CartItem> cartItems = cartItemRepository.findByCart(cart);

		// if(cartItems.isEmpty()) {
		// 	throw new CartItem
		// }

		List<Long> itemIds = cartRequestDTO.getCartItems().stream().map(CartItemRequestDTO::getItemId).toList();

		cartItems.stream().map(cartItem ->{
			cartItem.getItem().getId()
		})
	}
}
