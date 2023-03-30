package com.devmatch.dto;

import java.time.format.DateTimeFormatter;

import com.devmatch.entity.Member;
import com.devmatch.entity.MemberImg;
import com.devmatch.entity.Rating;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDto {
	private Long id;
	
	private String memberName;
	
	private String memberImgUrl;

	private Integer score;
	
	private String content;
	
	private String regDate;
	
	@QueryProjection
	public RatingDto(Rating rating, MemberImg memberImg) {
		Member member = rating.getRequest().getCustomer();
		this.id = rating.getId();
		this.memberName = member.getLast_name() + member.getFirst_name();
		this.score = rating.getScore();
		this.content = rating.getContent();
		this.regDate = rating.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		if (memberImg == null)
			this.memberImgUrl = null;
		else 
			this.memberImgUrl = memberImg.getImgUrl();
	}
}
