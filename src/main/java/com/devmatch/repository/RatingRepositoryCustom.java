package com.devmatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.RatingDto;

public interface RatingRepositoryCustom {
	Page<RatingDto> getRatingDtoByProfileId(Long profileId, Pageable pageable);
	Double getAverageByMemberId(Long memberId);
}
