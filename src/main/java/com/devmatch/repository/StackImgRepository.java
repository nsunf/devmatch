package com.devmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.StackImg;

public interface StackImgRepository extends JpaRepository<StackImg, Long> {
	StackImg findByStackId(Long stackId);
}
