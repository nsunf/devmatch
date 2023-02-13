package com.devmatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.PortfolioImg;

public interface PortfolioImgRepository extends JpaRepository<PortfolioImg, Long> {
	List<PortfolioImg> findByPortfolioId(Long portfolioId);
}
