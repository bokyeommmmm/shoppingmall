package com.hana7.hanaro.entity;

public enum ORDERSTATUS {
	PAID("결제 완료"),
	PRE_DELIVER("배송 준비"),
	ING_DELIVER("배송 중"),
	AFTER_DELIVER("배송 완료");

	private final String description;

	ORDERSTATUS(String description) {
		this.description=description;
	}
}
