package com.devmatch.dto;

import com.devmatch.entity.StackImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackImgDto {
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	public StackImgDto(StackImg stackImg) {
		this.id = stackImg.getId();
		this.imgName = stackImg.getImgName();
		this.oriImgName = stackImg.getOriImgName();
		this.imgUrl = stackImg.getImgUrl();
	}
}
