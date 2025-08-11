package com.hana7.hanaro.exception.NotFound;

public class OrderNotFoundException extends NotFoundException {
	public OrderNotFoundException() {
		super("주문내역을 찾을 수 없습니다.");
	}
}
