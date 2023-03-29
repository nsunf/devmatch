package com.devmatch.entity;

import java.time.LocalDate;

import javax.persistence.*;

import com.devmatch.constant.RequestType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Request extends BaseEntity {
	@Id
	@Column(name = "request_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Member customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_id", nullable = false)
	private Member provider;

	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private Integer reward;
	
	@Column(nullable = false)
	private LocalDate deadline;

	@Column(nullable = false)
	private String content;

	@Column(nullable = true)
	private String titleTmp;
	
	@Column(nullable = true)
	private Integer rewardTmp;
	
	@Column(nullable = true)
	private LocalDate deadlineTmp;

	@Column(nullable = true)
	private String contentTmp;
	
	@Enumerated(EnumType.STRING)
	private RequestType status;
	
	@Column(nullable = false)
	private boolean accepted;
	
	public void adjustTmp() {
		this.title = this.titleTmp;
		this.reward = this.rewardTmp;
		this.deadline = this.deadlineTmp;
		this.content = this.contentTmp;
		
		this.titleTmp = null;
		this.rewardTmp = null;
		this.deadlineTmp = null;
		this.contentTmp = null;
	}
	
	public void revert() {
		this.titleTmp = null;
		this.rewardTmp = null;
		this.deadlineTmp = null;
		this.contentTmp = null;
	}
}
