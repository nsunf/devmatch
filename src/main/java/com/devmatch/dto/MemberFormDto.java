package com.devmatch.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.devmatch.constant.MemberRole;
import com.devmatch.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
	private MemberRole role;
	
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식에 맞게 입력해주세요.")
	private String email;
	
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(message = "비밀번호는 최소 8자 이상입니다.", min = 8)
	private String password;
	
	@NotBlank(message = "성을 입력해주세요.")
	private String last_name;
	
	@NotBlank(message = "이름을 입력해주세요.")
	private String first_name;
	
	@NotBlank(message = "주민번호를 입력해주세요.")
	@Size(message = "주민번호 형식에 맞게 입력해주세요.", min = 6, max = 6)
	private String ssn_1;
	
	@NotBlank(message = "주민번호를 입력해주세요.")
	@Size(message = "주민번호 형식에 맞게 입력해주세요.", min = 7, max = 7)
	private String ssn_2;
	
	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(message = "전화번호 형식에 맞게 입력해주세요.", min = 3, max = 3)
	private String phone_1; 

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(message = "전화번호 형식에 맞게 입력해주세요.", min = 3, max = 4)
	private String phone_2; 

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(message = "전화번호 형식에 맞게 입력해주세요.", min = 4, max = 4)
	private String phone_3; 
	
	public Member toEntity(PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setEmail(this.email);
		member.setSsn(String.join("-", this.ssn_1, this.ssn_2));
		member.setPhone(String.join("-", this.phone_1, this.phone_2, this.phone_3));
		member.setFirst_name(first_name);
		member.setLast_name(last_name);
		
		member.setPassword(passwordEncoder.encode(password));
		member.setRole(role);

		return member;
	}
}
