package com.hana7.hanaro.enums;

import lombok.Getter;

@Getter
public enum ORDERSTATUS {
	PAID("결제 완료"),
	PREPARING("배송 준비"),
	DELIVERING("배송중"),
	DELIVERED("배송 완료");

	private final String description;

	ORDERSTATUS(String description) {
		this.description = description;

	}

	public ORDERSTATUS getNextState() {
		return switch (this) {
			case PAID -> PREPARING;
			case PREPARING -> DELIVERING;
			case DELIVERING -> DELIVERED;
			default -> throw new IllegalStateException("Cannot determine next status for: " + this);
		};
	}

	public int stateInterval() {
		return switch (this) {
			case PAID -> 5;
			case PREPARING -> 15;
			case DELIVERING -> 60;
			default -> -1;
		};
	}
}
