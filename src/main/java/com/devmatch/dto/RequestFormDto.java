package com.devmatch.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.devmatch.constant.RequestType;
import com.devmatch.entity.Request;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestFormDto {
	private Long id;
	
	@NotNull
	private Long profileId;
	
	@NotBlank
	private String title;
	
	@NotNull
	@Min(value = 0)
	private Integer reward;

	@NotBlank
	private String deadline;
	
	@NotBlank
	private String content;

	private RequestType status;
	
//	private boolean accepted;
}
