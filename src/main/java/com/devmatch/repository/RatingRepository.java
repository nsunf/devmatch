package com.devmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	Rating findByRequestId(Long requestId);
}
