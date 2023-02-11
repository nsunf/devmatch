package com.devmatch.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.ProfileCardDto;
import com.devmatch.dto.QProfileCardDto;
import com.devmatch.entity.QMember;
import com.devmatch.entity.QMemberImg;
import com.devmatch.entity.QProfile;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {
	
	private JPAQueryFactory qf;
	
	public ProfileRepositoryCustomImpl(EntityManager em) {
		this.qf = new JPAQueryFactory(em);
	}

	@Override
	public Page<ProfileCardDto> getProfileCardDtoList(String searchQuery, List<Long>stackIdList, Pageable pageable) {
		QProfile profile = QProfile.profile;
		QMember member = QMember.member;
		QMemberImg memberImg = QMemberImg.memberImg;
		
		JPAQuery<ProfileCardDto> q = qf
				.select(new QProfileCardDto(profile.id, member.first_name, member.last_name, memberImg.imgUrl, profile.title, profile.subTitle))
				.from(profile)
				.join(profile.member, member)
				.leftJoin(memberImg)
				.on(memberImg.member.eq(member))
				.orderBy(profile.updateTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize());
		
		

		List<ProfileCardDto> contents = qf
				.select(new QProfileCardDto(profile.id, member.first_name, member.last_name, memberImg.imgUrl, profile.title, profile.subTitle))
				.from(profile)
				.join(profile.member, member)
				.leftJoin(memberImg)
				.on(memberImg.member.eq(member))
				.orderBy(profile.updateTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long total = qf.select(Wildcard.count).from(profile).fetchFirst();
		
		return new PageImpl<>(contents, pageable, total);
	}

}
