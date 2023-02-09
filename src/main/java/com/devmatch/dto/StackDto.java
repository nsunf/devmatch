package com.devmatch.dto;

import com.devmatch.entity.Stack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackDto {
	private Long id;
	
	private String name;
	
	private String imgUrl;

	public StackDto(Stack stack) {
		this.id = stack.getId();
		this.name = stack.getName();
		this.imgUrl = stack.getStackImg().getImgUrl();
	}
}
