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
	private int reward;
	
	@Column(nullable = false)
	private LocalDate deadline;

	@Column(nullable = false)
	private String content;
	
	@Enumerated(EnumType.STRING)
	private RequestType status;
	
	@Column(nullable = false)
	private boolean accepted;
}
