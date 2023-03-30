package com.devmatch.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.dto.RatingDto;
import com.devmatch.dto.RatingFormDto;
import com.devmatch.entity.Member;
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
	private final MemberService memberService;
	
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
	
	@Transactional(readOnly = true)
	public RatingFormDto getRatingFormDto(Long requestId) {
		Rating rating = ratingRepo.findByRequestId(requestId);
		return rating == null ? new RatingFormDto() : new RatingFormDto(rating);
	}
	
	@Transactional(readOnly = true)
	public Page<RatingDto> getRatingDtoList(Long profileId, Pageable pageable) {
		return ratingRepo.getRatingDtoByProfileId(profileId, pageable);
	}

	@Transactional(readOnly = true)
	public Double getAverage() {
		Member member = memberService.getMember();
		return ratingRepo.getAverageByMemberId(member.getId());
	}
}
