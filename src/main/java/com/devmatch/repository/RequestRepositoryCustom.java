package com.devmatch.repository;

import java.util.List;

import com.devmatch.dto.MonthlyRequestDto;

public interface RequestRepositoryCustom {
	List<MonthlyRequestDto> getMonthlyRequestDtoList();
}
