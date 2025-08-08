package com.hana7.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

}
