package com.devmatch.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile extends BaseEntity {
	
	@Id
	@Column(name = "profile_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	private String title;
	
	private String subTitle;

	private String content;
}
