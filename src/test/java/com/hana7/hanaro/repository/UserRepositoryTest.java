package com.hana7.hanaro.repository;

import com.hana7.hanaro.config.QueryDSLConfig;
import com.hana7.hanaro.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(QueryDSLConfig.class)
class UserRepositoryTest extends RepositoryTest{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserCustomRepositoryImpl userCustomRepository;

	@Test
	void findAll() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		Page<User> users = userRepository.findAll(pageable);

		// then
		// Assertions.assertThat(users.getTotalElements()).isEqualTo(5);
		System.out.println(users.getContent());
	}

	@Test
	void findUser() {
		// given
		Pageable pageable = PageRequest.of(0, 10);

		// when
		Page<User> users = userRepository.findByNameAndEmail("김보", pageable);

		// then
		Assertions.assertThat(users.getTotalElements()).isEqualTo(1);
	}


}
