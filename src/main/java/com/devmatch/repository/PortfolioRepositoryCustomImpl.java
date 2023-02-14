package com.devmatch.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.PortfolioDto;
import com.devmatch.dto.QPortfolioDto;
import com.devmatch.entity.QMember;
import com.devmatch.entity.QMemberImg;
import com.devmatch.entity.QPortfolio;
import com.devmatch.entity.QProfile;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class PortfolioRepositoryCustomImpl implements PortfolioRepositoryCustom {
	private JPAQueryFactory qf;
	
	public PortfolioRepositoryCustomImpl(EntityManager em) {
		this.qf = new JPAQueryFactory(em);
	}

	@Override
	public Page<PortfolioDto> getPortfolioDtoList(String searchQuery, Pageable pageable) {

		QPortfolio portfolio = QPortfolio.portfolio;
		QMember member = QMember.member;
		QMemberImg memberImg = QMemberImg.memberImg;
		QProfile profile = QProfile.profile;
		
		List<PortfolioDto> content = qf
				.select(new QPortfolioDto(portfolio, member, memberImg.imgUrl, profile.id))
				.from(portfolio)
				.join(portfolio.member, member)
				.join(portfolio.profile, profile)
				.leftJoin(memberImg)
				.on(member.eq(memberImg.member))
				.where(portfolio.title.contains(searchQuery).or(portfolio.content.contains(searchQuery).or(portfolio.member.last_name.append(portfolio.member.first_name).contains(searchQuery))))
				.orderBy(portfolio.updateTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long total = qf.select(Wildcard.count).from(portfolio).fetchFirst();

		return new PageImpl<>(content, pageable, total);
	}
}
