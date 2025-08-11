package com.hana7.hanaro.repository;

import java.time.LocalDateTime;

import com.hana7.hanaro.enums.ORDERSTATUS;

public interface OrdersCustomRepository {
	long updateStateBatch(ORDERSTATUS state, ORDERSTATUS nextState, LocalDateTime timeToUp);
}
