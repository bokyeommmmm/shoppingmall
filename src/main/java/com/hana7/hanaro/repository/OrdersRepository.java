package com.hana7.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders,Long> {

}
