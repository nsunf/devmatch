package com.devmatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.ProfileStack;

public interface ProfileStackRepository extends JpaRepository<ProfileStack, Long> {
	List<ProfileStack> findByProfileId(Long profileId);
	void deleteByProfileId(Long profileId);
}
