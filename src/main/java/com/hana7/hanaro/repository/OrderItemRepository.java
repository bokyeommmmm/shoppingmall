package com.hana7.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.OrderItem;
import com.hana7.hanaro.entity.Orders;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findByOrder(Orders order);
}
