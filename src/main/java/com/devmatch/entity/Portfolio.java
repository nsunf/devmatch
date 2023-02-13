package com.devmatch.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Portfolio extends BaseEntity {
	@Id
	@Column(name = "portfolio_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	

	private String title;
	
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	public void update(String title, String content, Profile profile, Member member) {
		this.title = title;
		this.content = content;
		this.profile = profile;
		this.member = member;
	}
}
