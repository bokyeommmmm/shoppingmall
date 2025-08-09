package com.hana7.hanaro.exception.BadRequest;

import org.springframework.http.HttpStatus;

import com.hana7.hanaro.exception.HanaException;

public class BadRequestException extends HanaException {
	public BadRequestException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
