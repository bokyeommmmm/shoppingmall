package com.hana7.hanaro.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HanaException extends RuntimeException {
	private final HttpStatus httpStatus;
	private final String message;

	public HanaException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
