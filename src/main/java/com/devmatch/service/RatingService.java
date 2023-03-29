package com.devmatch.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.dto.RatingFormDto;
import com.devmatch.entity.Rating;
import com.devmatch.entity.Request;
import com.devmatch.repository.RatingRepository;
import com.devmatch.repository.RequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {
	private final RatingRepository ratingRepo;
	private final RequestRepository requestRepo;
	
	public void rate(RatingFormDto formDto) {
		Long ratingId = formDto.getId();
		if (ratingId == null) {
			Rating rating = new Rating();
			Request request = requestRepo.findById(formDto.getRequestId()).orElseThrow(EntityNotFoundException::new);
			rating.setRequest(request);
			rating.setScore(formDto.getScore());
			rating.setContent(formDto.getContent());
			
			ratingRepo.save(rating);
		} else {
			Rating rating = ratingRepo.findById(ratingId).orElseThrow(EntityNotFoundException::new);
			rating.setScore(formDto.getScore());
			rating.setContent(formDto.getContent());
		}
	}
	
	public RatingFormDto getRatingFormDto(Long requestId) {
		Rating rating = ratingRepo.findByRequestId(requestId);
		return rating == null ? new RatingFormDto() : new RatingFormDto(rating);
	}
}
