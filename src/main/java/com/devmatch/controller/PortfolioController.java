package com.devmatch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devmatch.service.PortfolioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {
	
	private final PortfolioService portfolioService;

	@GetMapping
	public String main(@RequestParam(required = false) String searchQuery, Model model) {
		model.addAttribute("searchQuery", searchQuery);

		return "portfolios/portfolioList";
	}
}
