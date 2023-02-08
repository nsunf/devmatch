package com.devmatch.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.devmatch.entity.Member;
import com.devmatch.entity.MemberImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMemberFormDto {
	private Long id;
	
	private String name;
	
	private String email;
	
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(message = "비밀번호는 최소 8자 이상입니다.", min = 8)
	private String password;
	
	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(message = "전화번호 형식에 맞게 입력해주세요.", min = 3, max = 3)
	private String phone_1;

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(message = "전화번호 형식에 맞게 입력해주세요.", min = 3, max = 4)
	private String phone_2;

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(message = "전화번호 형식에 맞게 입력해주세요.", min = 4, max = 4)
	private String phone_3;
	
	private MemberImgDto memberImgDto;
	
	public EditMemberFormDto(Member member, MemberImg memberImg) {
		this.id = member.getId();
		this.name = member.getLast_name() + member.getFirst_name();
		this.email = member.getEmail();

		String[] phoneArr = member.getPhone().split("-");
		this.phone_1 = phoneArr[0];
		this.phone_2 = phoneArr[1];
		this.phone_3 = phoneArr[2];
		
		if (memberImg != null) this.memberImgDto = new MemberImgDto(memberImg);
	}
	
	public Member updateMember(Member member, PasswordEncoder passwordEncoder) {
		member.setPassword(passwordEncoder.encode(this.password));
		member.setPhone(String.join("-", this.phone_1, this.phone_2, this.phone_3));

		return member;
	}
}
