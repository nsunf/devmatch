package com.devmatch.dto;

import java.time.format.DateTimeFormatter;

import com.devmatch.constant.RequestType;
import com.devmatch.entity.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {
	private Long id;
	
	private MemberDto provider;
	
	private String title;
	
	private String regDate;
	
	private int reward;
	
	private String deadline;
	
	private String content;
	
	private RequestType status;
	
	private boolean accepted;
	
	public RequestDto(Request request) {
		this.id = request.getId();
		this.provider = new MemberDto(request.getProvider());
		this.title = request.getTitle();
		this.reward = request.getReward();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		this.regDate = request.getRegTime().format(formatter);
		this.deadline = request.getDeadline().format(formatter);

		this.status = request.getStatus();
		this.accepted = request.isAccepted();
	}
}
