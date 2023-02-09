package com.devmatch.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.dto.ProfileFormDto;
import com.devmatch.dto.ProfileImgDto;
import com.devmatch.entity.Profile;
import com.devmatch.repository.ProfileImgRepository;
import com.devmatch.repository.ProfileRepository;
import com.devmatch.repository.ProfileStackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
	private final ProfileRepository profileRepo;
	private final ProfileImgRepository profileImgRepo;
	private final ProfileStackRepository profileStackRepo;
	
	@Transactional(readOnly = true)
	public ProfileFormDto getProfileFormDto(Long memberId) {
		Profile profile = profileRepo.findByMemberId(memberId).orElse(new Profile());
		List<ProfileImgDto> profileImgDtoList = profileImgRepo.findByProfileId(profile.getId()).stream().map(ProfileImgDto::new).toList();
		
		return new ProfileFormDto(profile, profileImgDtoList);
	}
}
