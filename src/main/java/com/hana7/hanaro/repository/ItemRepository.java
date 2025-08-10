package com.hana7.hanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Page<Item> findByItemNameContainsIgnoreCase(String itemName, Pageable pageable);

	Optional<Item> findById(Long id);
}
