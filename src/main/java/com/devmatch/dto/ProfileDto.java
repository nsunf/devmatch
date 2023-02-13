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

	@QueryProjection
	public ProfileDto(Long id, String title, String subTitle, String content, Long memberId, String email, String first_name, String last_name, String memberImgUrl) {
		this.id = id;
		this.title = title;
		this.subTitle = subTitle;
		this.content = content;
		
		MemberDto memberDto = new MemberDto();
		memberDto.setId(memberId);
		memberDto.setEmail(email);
		memberDto.setName(last_name + first_name);
		memberDto.setImgUrl(memberImgUrl);
		this.member = memberDto;
	}
}