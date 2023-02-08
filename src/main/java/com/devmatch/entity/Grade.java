package com.devmatch.entity;

import javax.persistence.*;

public class Grade {

	@Id
	@Column(name = "grade_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer gradeValue;

	private Integer numOfProj;
	
	private Integer totalPrice;
	
	private String gradeImgUrl;
}
