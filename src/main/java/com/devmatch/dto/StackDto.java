package com.devmatch.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackDto {
	private Long id;
	
	private String name;
	
	private String imgUrl;

	@QueryProjection
	public StackDto(Long id, String name, String imgUrl) {
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
	}
}
