package com.hana7.hanaro.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Getter
public class UserDTO extends User {

	private String email;
	private String nickname;
	private List<String> roleNames = new ArrayList<>();


	public UserDTO(String email, String password, String nickname, List<String> roleNames) {
		super(email, password, roleNames.stream().map(SimpleGrantedAuthority::new).toList());
		this.email = email;
		this.nickname = nickname;
		this.roleNames = roleNames;
	}

	public Map<String, Object> getClaims() {
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("nickname", nickname);
		map.put("roleNames", roleNames);

		return map;
	}

}
