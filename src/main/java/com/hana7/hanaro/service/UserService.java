package com.hana7.hanaro.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hana7.hanaro.dto.UserResponseDTO;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.exception.BadRequest.ItemDeleteBadRequestException;
import com.hana7.hanaro.exception.BadRequest.UserDeleteBadRequestException;
import com.hana7.hanaro.exception.NotFound.NotFoundException;
import com.hana7.hanaro.exception.NotFound.UserNotFoundException;
import com.hana7.hanaro.repository.UserCustomRepositoryImpl;
import com.hana7.hanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserCustomRepositoryImpl userCustomRepository;

	public Page<UserResponseDTO> getUsers(Pageable pageable) {

		Page<User> users = userRepository.findAll(pageable);
		if (users.isEmpty()) {
			throw new UserNotFoundException();
		}

		return users.map(UserService::toDto);
	}
	public Page<UserResponseDTO> findUserByKeyword(String keyword, Pageable pageable) throws NotFoundException {
		Page<User> users = userCustomRepository.findByNameAndEmail(keyword, pageable);
		if(users.isEmpty()){
			throw  new UserNotFoundException();
		}
		return users.map(UserService::toDto);
	}

	public void deleteUserById(long id) throws NotFoundException {
		User user = userRepository.findById(id).orElse(null);
		if(user == null){
			throw  new UserNotFoundException();
		}
		try {
			userRepository.delete(user);
		} catch (Exception e) {
			throw new UserDeleteBadRequestException();
		}

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
