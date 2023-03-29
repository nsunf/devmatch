package com.devmatch.dto;

import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.devmatch.constant.RequestType;
import com.devmatch.entity.Request;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
	
	private boolean accepted;
	
	public RequestFormDto(Request request) {
		this.id = request.getId();
		this.profileId = request.getProvider().getId();
		this.title = request.getTitle();
		this.reward = request.getReward();
		this.deadline = request.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.content = request.getContent();
		this.status = request.getStatus();
		this.accepted = request.isAccepted();
	}
}
