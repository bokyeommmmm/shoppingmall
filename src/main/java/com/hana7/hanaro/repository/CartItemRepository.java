package com.hana7.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

	List<CartItem> findByCart(Cart cart);

	void deleteByCart(Cart cart);
}
