package com.devmatch.service;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.StackImgDto;
import com.devmatch.entity.StackImg;
import com.devmatch.repository.StackImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StackImgService {
	@Value("${stackImgLocation}")
	private String stackImgLocation;

	private final StackImgRepository stackImgRepo;
	private final FileService fileService;
	
	public void saveImg(StackImg stackImg, MultipartFile stackImgFile) throws IOException {
		String oriImgName = stackImgFile.getOriginalFilename();
		String imgName = fileService.uploadFile(stackImgLocation, oriImgName, stackImgFile.getBytes());
		String imgUrl = "/local/images/stack/" + imgName;
		
		stackImg.updateImage(imgName, imgUrl, oriImgName);
		stackImgRepo.save(stackImg);
	}
	
	public StackImgDto getStackImgDto(Long stackId) {
		StackImg stackImg = stackImgRepo.findByStackId(stackId);
		return new StackImgDto(stackImg);
	}
	
	public void updateImg(Long stackId, MultipartFile stackImgFile) throws IOException {
		if (stackImgFile != null && !stackImgFile.isEmpty()) {
			StackImg stackImg = stackImgRepo.findByStackId(stackId);
			
			fileService.deleteFile(stackImgLocation + "/" + stackImg.getImgName());
			
			String oriImgName = stackImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(stackImgLocation, oriImgName, stackImgFile.getBytes());
			String imgUrl = "/local/images/stack/" + imgName;
			
			stackImg.update(imgName, imgUrl, oriImgName);
		}
	}
}
