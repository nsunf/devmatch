package com.devmatch.dto;

import com.devmatch.entity.ProfileStack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileStackDto {
	private Long id;

	private Long stackId;

	private String stackName;
	
	private String stackImgUrl;
	
	public ProfileStackDto(ProfileStack profileStack) {
		this.id = profileStack.getId();
		this.stackId = profileStack.getStack().getId();
		this.stackName = profileStack.getStack().getName();
		this.stackImgUrl = profileStack.getStack().getStackImg().getImgUrl();
	}
}
