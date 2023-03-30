package com.devmatch.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.QRatingDto;
import com.devmatch.dto.RatingDto;
import com.devmatch.entity.QMember;
import com.devmatch.entity.QMemberImg;
import com.devmatch.entity.QRating;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class RatingRepositoryCustomImpl implements RatingRepositoryCustom {
	private JPAQueryFactory qf;

	public RatingRepositoryCustomImpl(EntityManager em) {
		this.qf = new JPAQueryFactory(em);
	}

	@Override
	public Page<RatingDto> getRatingDtoByProfileId(Long profileId, Pageable pageable) {
		QRating rating = QRating.rating;
		QMemberImg memberImg = QMemberImg.memberImg;
		
		List<RatingDto> contents = qf
				.select(new QRatingDto(rating, memberImg))
				.from(rating)
				.leftJoin(memberImg)
				.on(rating.request.customer.id.eq(memberImg.member.id))
				.orderBy(rating.regTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		
		long total = qf
				.select(Wildcard.count)
				.from(rating)
				.leftJoin(memberImg)
				.on(rating.request.customer.id.eq(memberImg.member.id))
				.fetchOne();

		return new PageImpl<>(contents, pageable, total);
	}
}
