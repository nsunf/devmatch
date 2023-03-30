package com.devmatch.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.ProfileCardDto;
import com.devmatch.dto.ProfileDto;
import com.devmatch.dto.QProfileCardDto;
import com.devmatch.dto.QProfileDto;
import com.devmatch.entity.QMember;
import com.devmatch.entity.QMemberImg;
import com.devmatch.entity.QProfile;
import com.devmatch.entity.QProfileStack;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {
	
	private JPAQueryFactory qf;
	
	public ProfileRepositoryCustomImpl(EntityManager em) {
		this.qf = new JPAQueryFactory(em);
	}
	
	public BooleanExpression containName(String searchQuery) {
		QMember member = QMember.member;
		QProfile profile = QProfile.profile;
		return member.last_name
				.append(member.first_name)
				.contains(searchQuery)
				.or(profile.title.contains(searchQuery))
				.or(profile.subTitle.contains(searchQuery))
				.or(profile.content.contains(searchQuery));
	}
	
	public BooleanExpression inStackIdList(QProfile profile, List<Long> stackIdList) {
		QProfileStack profileStack = QProfileStack.profileStack;
		JPAQuery<Long> profileStackIdList = qf.select(profileStack.stack.id).from(profileStack).where(profileStack.profile.eq(profile));
		
		BooleanExpression result = null;
		for (int i = 0; i < stackIdList.size(); i++) {
			if (i == 0)
				result = profileStackIdList.contains(stackIdList.get(i));
			else
				result = result.and(profileStackIdList.contains(stackIdList.get(i)));
		}
		
		return result;
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
				.on(memberImg.member.eq(member));
		
		if (searchQuery != null)
			q.where(containName(searchQuery));
	
		if (stackIdList != null)
			q.where(inStackIdList(profile, stackIdList));
		
		List<ProfileCardDto> contents = q
				.orderBy(profile.updateTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long total = qf.select(Wildcard.count).from(profile).fetchFirst();
		
		return new PageImpl<>(contents, pageable, total);
	}
	
	@Override
	public ProfileCardDto getProfileCardDtoByMemberId(Long memberId) {
		QProfile profile = QProfile.profile;
		QMember member = QMember.member;
		QMemberImg memberImg = QMemberImg.memberImg;
		
		ProfileCardDto content = qf
				.select(new QProfileCardDto(profile.id, member.first_name, member.last_name, memberImg.imgUrl, profile.title, profile.subTitle))
				.from(profile)
				.join(member)
				.on(profile.member.id.eq(member.id))
				.leftJoin(memberImg)
				.on(member.id.eq(memberImg.member.id))
				.where(member.id.eq(memberId))
				.fetchFirst();

		return content;
	}

	@Override
	public ProfileDto getProfileDto(Long profileId) {
		QProfile profile = QProfile.profile;
		QMember member = QMember.member;
		QMemberImg memberImg = QMemberImg.memberImg;
		
		ProfileDto result = qf.
				select(new QProfileDto(profile.id, profile.title, profile.subTitle, profile.content, member.id, member.email, member.first_name, member.last_name, memberImg.imgUrl))
				.from(profile)
				.join(profile.member, member)
				.leftJoin(memberImg)
				.on(memberImg.member.eq(member))
				.where(profile.id.eq(profileId))
				.fetchOne();
		
		return result;
	}
}
