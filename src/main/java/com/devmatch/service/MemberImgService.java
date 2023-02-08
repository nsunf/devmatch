package com.devmatch.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.entity.MemberImg;
import com.devmatch.repository.MemberImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberImgService {
	
	@Value("${memberImgLocation}")
	private String memberImgLocation;

	private final MemberImgRepository memberImgRepo;
	private final FileService fileService;
	
	public void saveImg(MemberImg memberImg, MultipartFile memberImgFile) throws IOException {
		String oriImgName = memberImg.getOriImgName();
		String imgName = "";
		String imgUrl = "";
		
		imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
		imgUrl = "/local/images/member/" + imgName;
		
		memberImg.updateImage(imgName, imgUrl, oriImgName);
		memberImgRepo.save(memberImg);
	}
	
	public MemberImg getImg(Long memberId) {
		return memberImgRepo.findByMemberId(memberId).orElse(new MemberImg());
	}
}
