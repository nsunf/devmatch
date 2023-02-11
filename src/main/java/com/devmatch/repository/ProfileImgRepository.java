package com.devmatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.ProfileImg;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {
	List<ProfileImg> findByProfileId(Long profileId);
	void deleteByProfileId(Long profileId);
}
