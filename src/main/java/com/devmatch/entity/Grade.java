package com.devmatch.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Grade {
	@Id
	@Column(name = "grade_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private Integer gradeValue;

	@Column(nullable = false)
	private Long refScore;
	
	@Column(nullable = false)
	private String gradeImgUrl;
}
