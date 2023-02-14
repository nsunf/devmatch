package com.devmatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.PortfolioDto;

public interface PortfolioRepositoryCustom {
	Page<PortfolioDto> getPortfolioDtoList(String searchQuery, Pageable pageable);
}
