package com.devmatch.dto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.devmatch.entity.Portfolio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PortfolioDto {
	private Long id;
	
	private String title;
	
	private String content;
	
	private String regDate;
	
	private List<String> imgUrlList = new ArrayList<>();

	public PortfolioDto(Portfolio portfolio, List<String> imgUrlList) {
		this.id = portfolio.getId();
		this.title = portfolio.getTitle();
		this.content = portfolio.getContent();
		this.regDate = portfolio.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		this.imgUrlList = imgUrlList;
	}
}
