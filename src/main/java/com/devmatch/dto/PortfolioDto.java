package com.devmatch.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.devmatch.entity.Member;
import com.devmatch.entity.Portfolio;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PortfolioDto {
	private Long id;
	
	private String title;
	
	private String content;
	
	private String regDate;

	private Long profileId;
	
	private MemberDto memberDto;
	
	private List<String> imgUrlList = new ArrayList<>();

	public PortfolioDto(Portfolio portfolio, List<String> imgUrlList) {
		this.id = portfolio.getId();
		this.title = portfolio.getTitle();
		this.content = portfolio.getContent();
		this.regDate = portfolio.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		this.imgUrlList = imgUrlList;
	}
	
	@QueryProjection
	public PortfolioDto(Portfolio portfolio, Member member, String memberImgUrl, Long profileId) {
		this.id = portfolio.getId();
		this.title = portfolio.getTitle();
		this.content = portfolio.getContent();
		this.regDate = portfolio.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		
		this.memberDto = new MemberDto(member);
		this.memberDto.setImgUrl(memberImgUrl);
		
		this.profileId = profileId;
	}
}
