package com.hana7.hanaro.exception.NotFound;

import org.springframework.http.HttpStatus;

import com.hana7.hanaro.exception.HanaException;

public class NotFoundException extends HanaException {
	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
