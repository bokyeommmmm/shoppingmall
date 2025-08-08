package com.hana7.hanaro.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hana7.hanaro.entity.User;

public interface UserCustomRepository {
	Page<User> findByNameAndEmail(String keyword, Pageable pageable);
}
