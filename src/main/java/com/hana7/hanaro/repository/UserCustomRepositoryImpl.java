package com.hana7.hanaro.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.hana7.hanaro.entity.QUser;
import com.hana7.hanaro.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public UserCustomRepositoryImpl( JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public Page<User> findByNameAndEmail(String keyword, Pageable pageable) {
		QUser qUser = QUser.user;

		List<User> users = jpaQueryFactory.selectFrom(qUser)
			.where(qUser.userName.containsIgnoreCase(keyword)
				.or(qUser.email.containsIgnoreCase(keyword)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = jpaQueryFactory.select(qUser.count())
			.from(qUser)
			.where(qUser.userName.containsIgnoreCase(keyword)
				.or(qUser.email.containsIgnoreCase(keyword)))
			.fetchOne();
		if(count==null){
			count =0L;
		}


		return new PageImpl<>(users, pageable, count);
	}
}
