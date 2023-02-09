package com.devmatch.dto;

import java.util.List;

import com.devmatch.entity.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileFormDto {
	private Long id;
	
	private String title;
	
	private String subTitle;
	
	private String content;

	private List<ProfileImgDto> profileImgDtoList;

	private List<StackDto> stackDtoList;
	
	public ProfileFormDto(Profile profile, List<ProfileImgDto> profileImgDtoList) {
		this.id = profile.getId();
		this.title = profile.getTitle();
		this.subTitle = profile.getSubTitle();
		this.content = profile.getContent();
		this.profileImgDtoList = profileImgDtoList;
	}
}
