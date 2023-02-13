package com.devmatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
	List<Portfolio> findByMemberId(Long memberId);
}
