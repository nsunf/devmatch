package com.devmatch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devmatch.dto.ProfileCardDto;

public interface ProfileRepositoryCustom {
	Page<ProfileCardDto> getProfileCardDtoList(String searchQuery, List<Long> stackIdList, Pageable pageable);
}
