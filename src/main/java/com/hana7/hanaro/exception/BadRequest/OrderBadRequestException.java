package com.hana7.hanaro.exception.BadRequest;


public class OrderBadRequestException extends BadRequestException {
	public OrderBadRequestException() {
		super("재고가 부족합니다.");
	}
}
