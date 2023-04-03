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
	
	private Integer gradeValue;
	
	private String gradeImgUrl;

	@QueryProjection
//	public ProfileCardDto(Long profileId, String first_name, String last_name, String memberImgUrl, String title, String subTitle) {
	public ProfileCardDto(
			Long profileId,
			String first_name,
			String last_name,
			String memberImgUrl,
			String title,
			String subTitle,
			Integer gradeValue,
			String gradeImgUrl)
	{
		this.profileId = profileId;
		this.memberName = last_name + first_name;
		this.memberImgUrl = memberImgUrl;
		this.title = title;
		this.subTitle = subTitle;
		this.gradeValue = gradeValue;
		this.gradeImgUrl = gradeImgUrl;
	}
}
