package com.devmatch.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.ProfileCardDto;
import com.devmatch.dto.ProfileDto;
import com.devmatch.dto.ProfileFormDto;
import com.devmatch.dto.ProfileImgDto;
import com.devmatch.dto.StackDto;
import com.devmatch.entity.Member;
import com.devmatch.entity.Profile;
import com.devmatch.entity.ProfileImg;
import com.devmatch.entity.ProfileStack;
import com.devmatch.entity.Stack;
import com.devmatch.repository.ProfileRepository;
import com.devmatch.repository.ProfileStackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
	private final ProfileRepository profileRepo;
	private final ProfileStackRepository profileStackRepo;
	private final ProfileImgService profileImgService;
	private final StackService stackService;
	private final MemberService memberService;

	@Transactional(readOnly = true)
	public Profile getProfile(Long id) {
		return profileRepo.findById(id).orElseThrow(EntityNotFoundException::new);
	}
	
	@Transactional(readOnly = true)
	public ProfileFormDto getProfileFormDto(Long memberId) {
		Profile profile = profileRepo.findByMemberId(memberId).orElse(new Profile());
		List<ProfileImgDto> profileImgDtoList = profileImgService.getProfileImgDtoListByProfileId(profile.getId());
		
		return new ProfileFormDto(profile, profileImgDtoList);
	}
	
	public void updateProfile(Member member, ProfileFormDto profileFormDto, List<MultipartFile> profileImgFileList) throws IOException {
		Profile	profile = profileRepo.findByMemberId(member.getId()).orElse(null);
		if (profile == null) {
			profile = new Profile();
			profile.setMember(member);
		}
		
		profile.update(profileFormDto.getTitle(), profileFormDto.getSubTitle(), profileFormDto.getContent());
		profileRepo.save(profile);
		
		if (!profileImgFileList.isEmpty() && !profileImgFileList.get(0).isEmpty()) {
			profileImgService.deleteImgs(profile.getId());
			
			for (MultipartFile profileImgFile : profileImgFileList) {
				ProfileImg profileImg = new ProfileImg();
				profileImg.setProfile(profile);
				profileImgService.saveImg(profileImg, profileImgFile);
			}
		}
		
		List<Long> stackIdList = profileFormDto.getStackIdList();
		profileStackRepo.deleteByProfileId(profile.getId());
		
		
		for (Long stackId: stackIdList) {
			Stack stack = stackService.getStackById(stackId);
			ProfileStack profileStack = new ProfileStack();
			profileStack.setProfile(profile);
			profileStack.setStack(stack);
			profileStackRepo.save(profileStack);
		}
	}
	
	@Transactional(readOnly = true)
	public Page<ProfileCardDto> getProfileCardDtoList(String searchQuery, List<Long> stackIdList, Pageable pageable) {
		return profileRepo.getProfileCardDtoList(searchQuery, stackIdList, pageable);
	}

	@Transactional(readOnly = true)
	public ProfileDto getProfileDto(Long profileId) {
		ProfileDto profileDto = profileRepo.getProfileDto(profileId); 
		List<StackDto> stackDtoList = stackService.getStackDtoListByProfileId(profileId);
		List<ProfileImgDto> profileImgDtoList = profileImgService.getProfileImgDtoListByProfileId(profileId);
		
		profileDto.setStackDtoList(stackDtoList);
		profileDto.setProfileImgDtoList(profileImgDtoList);
		
		return profileDto;
	}

	@Transactional(readOnly = true)
	public boolean isExist() {
		Member member = memberService.getMember();
		Profile profile = profileRepo.findByMemberId(member.getId()).orElse(null);
		
		return profile != null;
	}
}
