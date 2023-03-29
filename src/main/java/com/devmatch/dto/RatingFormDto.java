package com.devmatch.dto;

import com.devmatch.entity.Rating;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingFormDto {
	private Long id;
	
	private Long requestId;
	
	private Integer score;
	
	private String content;
	
	public RatingFormDto(Rating rating) {
		this.id = rating.getId();
		this.requestId = rating.getRequest().getId();
		this.score = rating.getScore();
		this.content = rating.getContent();
	}
}
