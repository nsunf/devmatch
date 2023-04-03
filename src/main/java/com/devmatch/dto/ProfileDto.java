package com.devmatch.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
	private Long id;
	
	private String title;

	private String subTitle;

	private String content;
	
	private MemberDto member;
	
	private List<StackDto> stackDtoList;
	
	private List<ProfileImgDto> profileImgDtoList;

	private Integer gradeValue;
	
	private String gradeImgUrl;

	@QueryProjection
	public ProfileDto(
			Long id,
			String title,
			String subTitle,
			String content,
			Long memberId,
			String email,
			String first_name,
			String last_name,
			String memberImgUrl,
			Integer gradeValue,
			String gradeImgUrl)
	{
		this.id = id;
		this.title = title;
		this.subTitle = subTitle;
		this.content = content;
		this.gradeValue = gradeValue;
		this.gradeImgUrl = gradeImgUrl;
		
		MemberDto memberDto = new MemberDto();
		memberDto.setId(memberId);
		memberDto.setEmail(email);
		memberDto.setName(last_name + first_name);
		memberDto.setImgUrl(memberImgUrl);
		this.member = memberDto;
	}
}