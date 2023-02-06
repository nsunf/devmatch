package com.devmatch.entity;

import javax.persistence.*;

import com.devmatch.constant.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, unique = true)
	private String ssn;
	
	private String phone;
	
	@Column(nullable = false)
	private String first_name;
	
	@Column(nullable = false)
	private String last_name;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberRole role;
}
