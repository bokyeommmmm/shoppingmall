package com.hana7.hanaro.entity;

import lombok.Getter;

@Getter
public enum USERROLE {
	ROLE_ADMIN("관리자"), ROLE_USER("유저");

	private final String description;
	USERROLE(String description) {
		this.description=description;
	}
}
