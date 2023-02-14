package com.devmatch.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devmatch.dto.PortfolioDto;
import com.devmatch.service.PortfolioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {
	
	private final PortfolioService portfolioService;

	@GetMapping("")
	public String main(@RequestParam(required = false) String searchQuery, Model model) {
		model.addAttribute("searchQuery", searchQuery);
		
		return "portfolios/portfolioList";
	}

	@GetMapping("/load")
	public @ResponseBody ResponseEntity<?> loadPartners(@RequestParam(required = false, defaultValue = "") String searchQuery , @RequestParam Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.orElse(0), 8);
		Page<PortfolioDto> portfolioDtoList = portfolioService.getPortfolioDtoPageable(searchQuery, pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("result", portfolioDtoList);
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
	}


	@GetMapping("/load/{portfolioId}")
	public @ResponseBody ResponseEntity<?> portfolioDetail(@PathVariable Long portfolioId) {
		PortfolioDto portfolioDto = portfolioService.getPortfolio(portfolioId);
		
		Map<String, PortfolioDto> map = new HashMap<>();
		map.put("result", portfolioDto);
		
		return new ResponseEntity<Map<String, PortfolioDto>>(map, HttpStatus.OK);
	}
}
