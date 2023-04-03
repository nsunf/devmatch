package com.devmatch.dto;

import java.time.format.DateTimeFormatter;

import com.devmatch.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
	private Long id;
	
	private String email;
	
	private String name;
	
	private String regDate;
	
	private String imgUrl;
	
	private Long point;
	
	public MemberDto(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.name = member.getLast_name() + member.getFirst_name();
		this.regDate = member.getRegTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		this.point = member.getPoint();
	}
}
