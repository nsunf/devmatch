package com.devmatch.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.devmatch.entity.Profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileFormDto {
	private Long id;
	
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	
	private String subTitle;
	
	private String content;

	private List<ProfileImgDto> profileImgDtoList;
	
//	private List<ProfileStackDto> profileStackDtoList;

	private List<Long> stackIdList;
	
	public ProfileFormDto(Profile profile, List<ProfileImgDto> profileImgDtoList) {
		if (profile != null) {
			this.id = profile.getId();
			this.title = profile.getTitle();
			this.subTitle = profile.getSubTitle();
			this.content = profile.getContent();
		}
		this.profileImgDtoList = profileImgDtoList;
	}
}
