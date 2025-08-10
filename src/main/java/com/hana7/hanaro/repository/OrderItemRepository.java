package com.hana7.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
