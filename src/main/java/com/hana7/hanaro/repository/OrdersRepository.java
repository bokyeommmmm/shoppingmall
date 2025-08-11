package com.hana7.hanaro.repository;

import java.time.LocalDateTime;

import com.hana7.hanaro.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hana7.hanaro.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT o FROM Orders o LEFT JOIN FETCH o.orderItems WHERE o.user = :user",
           countQuery = "SELECT count(o) FROM Orders o WHERE o.user = :user")
    Page<Orders> findByUser(@Param("user") User user, Pageable pageable);

    Page<Orders> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
