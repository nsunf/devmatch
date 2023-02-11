package com.devmatch.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.ProfileImgDto;
import com.devmatch.entity.ProfileImg;
import com.devmatch.repository.ProfileImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileImgService {

	@Value("${profileImgLocation}")
	public String profileImgLocation;
	
	private final ProfileImgRepository profileImgRepo;
	private final FileService fileService;
	
	public void saveImg(ProfileImg profileImg, MultipartFile profileImgFile) throws IOException {
		String oriImgName = profileImgFile.getOriginalFilename();
		String imgName = fileService.uploadFile(profileImgLocation, oriImgName, profileImgFile.getBytes());
		String imgUrl = "/local/images/profile/" + imgName;
		
		profileImg.updateImage(imgName, imgUrl, oriImgName);
		profileImgRepo.save(profileImg);
	}
	
	public void deleteImgs(Long profileId) {
		List<ProfileImg> profileImgs = profileImgRepo.findByProfileId(profileId);
		
		profileImgs.stream().forEach(pfi -> {
			fileService.deleteFile(profileImgLocation + "/" + pfi.getImgName());
			profileImgRepo.delete(pfi);
		});
	}
	
	public List<ProfileImgDto> getProfileImgDtoListByProfileId(Long profileId) {
		return profileImgRepo.findByProfileId(profileId).stream().map(ProfileImgDto::new).toList();
	}
}
