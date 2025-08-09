package com.hana7.hanaro.exception.BadRequest;


public class UserDeleteBadRequestException extends BadRequestException {
	public UserDeleteBadRequestException() {
		super("유저 삭제 실패.");
	}
}
