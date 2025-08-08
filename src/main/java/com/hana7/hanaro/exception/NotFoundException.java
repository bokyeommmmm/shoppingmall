package com.hana7.hanaro.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HanaException{
	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
