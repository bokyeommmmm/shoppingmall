package com.hana7.hanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.User;

public interface CartRepository extends JpaRepository<Cart,Long> {

	Optional<Cart> findByUser(User user);
}
