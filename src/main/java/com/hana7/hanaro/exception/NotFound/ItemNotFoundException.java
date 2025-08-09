package com.hana7.hanaro.exception.NotFound;

public class ItemNotFoundException extends NotFoundException {
	public ItemNotFoundException() {
		super("상품을 찾을 수 없습니다.");
	}
}
