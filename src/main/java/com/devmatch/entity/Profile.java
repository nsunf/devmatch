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
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
	@Lob
	private String title;
	
	private String subTitle;

	private String content;
	
	public void update(String title, String subTitle, String content) {
		this.title = title;
		this.subTitle = subTitle;
		this.content = content;
	}
}
