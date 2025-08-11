package com.hana7.hanaro.exception;

import org.springframework.http.HttpStatus;

public class UserExistException extends HanaException {
	public UserExistException(){
		super(HttpStatus.CONFLICT,"이미 존재하는 회원입니다.");

	}

}
