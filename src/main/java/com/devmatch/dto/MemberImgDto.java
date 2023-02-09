package com.devmatch.dto;

import com.devmatch.entity.MemberImg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberImgDto {
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	public MemberImgDto(MemberImg memberImg) {
		this.id = memberImg.getId();
		this.imgName = memberImg.getImgName();
		this.oriImgName = memberImg.getOriImgName();
		this.imgUrl = memberImg.getImgUrl();
	}
}
