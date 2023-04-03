package com.devmatch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.Grade;

public interface GradeRepository extends JpaRepository <Grade, Long> {
	Optional<Grade> findByGradeValue(Integer gradeValue);
	Grade findFirstByRefScoreLessThanOrderByRefScoreDesc(Long refScore);
}
