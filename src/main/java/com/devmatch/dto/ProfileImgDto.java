package com.devmatch.dto;

import com.devmatch.entity.ProfileImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileImgDto {
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	public ProfileImgDto(ProfileImg profileImg) {
		this.id = profileImg.getId();
		this.imgName = profileImg.getImgName();
		this.oriImgName = profileImg.getOriImgName();
		this.imgUrl = profileImg.getImgUrl();
	}
}
