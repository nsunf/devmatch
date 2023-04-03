package com.devmatch.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.devmatch.entity.Grade;
import com.devmatch.entity.Member;
import com.devmatch.repository.GradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeService {
	private final GradeRepository gradeRepo;

	public void initGrade() {
		this.setGrade(1, 0L, "/images/grade/Level=1.png");
		this.setGrade(2, 2500L, "/images/grade/Level=2.png");
		this.setGrade(3, 5000L, "/images/grade/Level=3.png");
		this.setGrade(4, 12000L, "/images/grade/Level=4.png");
		this.setGrade(5, 27000L, "/images/grade/Level=5.png");
	}
	
	public void setGrade(Integer gradeValue, Long refScore, String gradeImgUrl) {
		Grade grade = gradeRepo.findByGradeValue(gradeValue).orElse(new Grade());
		grade.setGradeValue(gradeValue);
		grade.setRefScore(refScore);
		grade.setGradeImgUrl(gradeImgUrl);
		gradeRepo.save(grade);
	}
	
	public void addPoint(Member member, Integer reward) {
		Double bonus = Math.ceil(reward * 0.0005);
		Long point = member.getPoint() + 1000 + bonus.longValue();
		member.setPoint(point);
		
		Grade grade = gradeRepo.findFirstByRefScoreLessThanOrderByRefScoreDesc(point);
		member.setGrade(grade);
	}
	
	public Grade getInitialGrade() {
		Grade grade = gradeRepo.findByGradeValue(1).orElse(null);
		if (grade == null) {
			this.initGrade();
			grade = gradeRepo.findByGradeValue(1).orElseThrow(EntityNotFoundException::new);
		}
		
		return grade;
	}
}
