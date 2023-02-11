package com.devmatch.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileCardDto {
	private Long profileId;

	private String memberName;
	
	private String memberImgUrl;
	
	private String title;
	
	private String subTitle;

	@QueryProjection
	public ProfileCardDto(Long profileId, String first_name, String last_name, String memberImgUrl, String title, String subTitle) {
		this.profileId = profileId;
		this.memberName = last_name + first_name;
		this.memberImgUrl = memberImgUrl;
		this.title = title;
		this.subTitle = subTitle;
	}
}
