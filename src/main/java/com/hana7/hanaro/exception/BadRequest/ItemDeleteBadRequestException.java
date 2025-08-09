package com.hana7.hanaro.exception.BadRequest;


public class ItemDeleteBadRequestException extends BadRequestException {
	public ItemDeleteBadRequestException() {
		super("상품삭제 실패.");
	}
}
