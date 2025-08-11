package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.CartItemResponseDTO;
import com.hana7.hanaro.dto.CartResponseDTO;
import com.hana7.hanaro.dto.ItemDetailResponseDTO;
import com.hana7.hanaro.exception.BadRequest.CartInsertBadRequestException;
import com.hana7.hanaro.exception.BadRequest.CartItemInsertBadRequestException;
import com.hana7.hanaro.exception.NotFound.CartItemNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana7.hanaro.dto.CartRequestDTO;
import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.exception.BadRequest.CartBadRequestException;
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

	// 장바구니 담기
	public void addItemToCart(CartRequestDTO cartRequestDTO, String email) {
		// user의 Cart가 있는지 확인하기
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		Cart cart = cartRepository.findByUser(user).orElse(null);

		if (cart == null) { // user가 cart를 가지고 있지 않다면 만들어주기
			cart = Cart.builder()
				.user(user)
				.build();
			try {
				cart = cartRepository.save(cart);
			} catch (Exception e) {
				throw new CartInsertBadRequestException();
			}
		}
		// stream 안에서 사용할 final 객체
		Cart finalCart = cart;
		List<CartItem> items = cartItemRepository.findByCart(finalCart);

		// DTO -> Entity
		List<CartItem> cartItems = cartRequestDTO.getCartItems().stream()
			.map((cartItem) -> {
				if(!items.isEmpty()){
					CartItem first = items.stream()
						.filter(item -> Objects.equals(item.getItem().getId(), cartItem.getItemId()))
						.findFirst().orElse(null);

					if(first != null){
						CartItem updated = first.toBuilder().amount(first.getAmount() + cartItem.getAmount()).build();
						cartItemRepository.save(updated);
						return updated;
					}
				}
				Item item = itemRepository.findById(cartItem.getItemId()).orElseThrow(ItemNotFoundException::new);
				return cartItem.toEntity(finalCart, item);
			}).toList();

		try {
			cartItemRepository.saveAll(cartItems);
		} catch (Exception e) {
			throw new CartItemInsertBadRequestException();
		}
	}

	public void updateCartItemAmount(Long cartItemId, int amount) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(CartItemNotFoundException::new);
		cartItem.setAmount(amount);
		cartItemRepository.save(cartItem);
	}

	public void deleteCartItem(Long cartItemId) {
		if (!cartItemRepository.existsById(cartItemId)) {
			throw new CartItemNotFoundException();
		}
		cartItemRepository.deleteById(cartItemId);
	}

	public CartResponseDTO getCart(String email){
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		Cart cart = cartRepository.findByUser(user).orElse(null);

		if(cart==null){
			Cart newCart = Cart.builder()
				.user(user)
				.build();
			newCart = cartRepository.save(newCart);

			return toDto(newCart,new ArrayList<>());
		}

		List<CartItem> items = cartItemRepository.findByCart(cart);

		return toDto(cart,items);
	}
	private CartResponseDTO toDto(Cart cart, List<CartItem> items){
		List<CartItemResponseDTO> cartItems = items.stream().map((item) -> CartItemResponseDTO.builder()
			.id(item.getItem().getId())
			.itemName(item.getItem().getItemName())
			.amount(item.getAmount())
			.build()).toList();

		return CartResponseDTO.builder()
			.id(cart.getId())
			.items(cartItems)
			.build();
	}
}
