package com.devmatch.dto;

import com.devmatch.entity.Stack;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StackFormDto {
	private Long id;

	private String name; 
	
	private StackImgDto stackImgDto;
	
	public Stack toStack() {
		Stack stack = new Stack();
		if (id != null) stack.setId(this.id);
		stack.setName(this.name);
		
		return stack;
	}
}
