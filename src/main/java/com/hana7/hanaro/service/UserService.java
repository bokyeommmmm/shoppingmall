package com.hana7.hanaro.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hana7.hanaro.dto.UserResponseDTO;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.exception.NotFoundException;
import com.hana7.hanaro.repository.UserCustomRepositoryImpl;
import com.hana7.hanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserCustomRepositoryImpl userCustomRepository;

	public List<UserResponseDTO> getUsers(){

		List<User> users = userRepository.findAll();
		if(users.isEmpty()){
			throw new NotFoundException("저장 된 회원이 없습니다!!!!!");
		}

		return users.stream()
			.map(UserService::toDto)
			.toList();
	}
	public Page<UserResponseDTO> findUserByKeyword(String keyword, Pageable pageable) throws NotFoundException {
		Page<User> users = userCustomRepository.findByNameAndEmail(keyword, pageable);
		if(users.isEmpty()){
			throw  new NotFoundException("조회 결과 없습니다.");
		}
		return users.map(UserService::toDto);
	}

	private static UserResponseDTO toDto(User user){
		return UserResponseDTO.builder()
			.id(user.getId())
			.userName(user.getUserName())
			.email(user.getEmail())
			.role(user.getRole().getDescription())
			.build();

	}
}
