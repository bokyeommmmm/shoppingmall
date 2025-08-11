package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.UserDTO;
import com.hana7.hanaro.entity.User;
import com.hana7.hanaro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username).orElse(null);

		if(user == null){
			throw new UsernameNotFoundException(username);
		}

		return new UserDTO(user.getEmail(), user.getPassword(), user.getUserName(), List.of(user.getRole().name()));
	}
}
