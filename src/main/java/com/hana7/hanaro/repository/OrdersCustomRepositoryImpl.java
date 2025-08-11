package com.hana7.hanaro.repository;

import com.hana7.hanaro.entity.QOrders;
import com.hana7.hanaro.enums.ORDERSTATUS;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class OrdersCustomRepositoryImpl implements OrdersCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public long updateStateBatch(ORDERSTATUS state, ORDERSTATUS nextState, LocalDateTime timeToUp) {
		QOrders orders = QOrders.orders;

		return jpaQueryFactory.update(orders)
			.set(orders.orderStatus, nextState)
			.set(orders.updatedAt, LocalDateTime.now())
			.where(orders.orderStatus.eq(state)
				.and(orders.updatedAt.before(timeToUp)))
			.execute();
	}
}
