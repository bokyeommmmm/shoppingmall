package com.hana7.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
