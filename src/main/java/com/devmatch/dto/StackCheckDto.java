package com.devmatch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackCheckDto {
	private Long stackId;

	private String stackName;
	
	private boolean checked;
	
	private String stackImgUrl;
	
}
