package com.devmatch.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import com.devmatch.dto.MonthlyRequestDto;
import com.devmatch.entity.QRequest;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class RequestRepositoryCustomImpl implements RequestRepositoryCustom {
	private JPAQueryFactory qf;
	
	public RequestRepositoryCustomImpl(EntityManager em) {
		this.qf = new JPAQueryFactory(em);
	}

	@Override
	public List<MonthlyRequestDto> getMonthlyRequestDtoList() {
		QRequest request = QRequest.request;
		Path<Integer> month = Expressions.numberPath(Integer.class, "month");
		
		List<MonthlyRequestDto> content = qf
				.select(
						Projections.fields(
								MonthlyRequestDto.class,
								request.regTime.month().as(month),
								request.id.count().as("count"))
				).from(request)
				.groupBy(month)
				.where(request.regTime.before(LocalDateTime.now()))
				.limit(6)
				.fetch();

		return content;
	}
}
