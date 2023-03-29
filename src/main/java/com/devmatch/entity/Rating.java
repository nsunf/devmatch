package com.devmatch.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rating extends BaseEntity {
	@Id
	@Column(name = "estimation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_id")
	private Request request;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private Member customer;
//	
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private Member provider;
	
	@Column(nullable = false)
	private Integer score;
	
	@Lob
	@Column(nullable = true)
	private String content;
}
