package com.devmatch.service;


import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.entity.PortfolioImg;
import com.devmatch.repository.PortfolioImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioImgService {
	
	@Value("${portfolioImgLocation}")
	private String portfolioImgLocation;

	private final PortfolioImgRepository portfolioImgRepo;
	private final FileService fileService;

	public void saveImg(PortfolioImg portfolioImg, MultipartFile portfolioImgFile) throws IOException {
		String oriImgName = portfolioImgFile.getOriginalFilename();
		String imgName = fileService.uploadFile(portfolioImgLocation, oriImgName, portfolioImgFile.getBytes());
		String imgUrl = "/local/images/portfolio/" + imgName;
		
		portfolioImg.updateImage(imgName, imgUrl, oriImgName);
		portfolioImgRepo.save(portfolioImg);
	}
	
	public void deleteImgs(Long portfolioId) {
		List<PortfolioImg> portfolioImgs = portfolioImgRepo.findByPortfolioId(portfolioId);
		System.out.println("portfolioImgs size --> " + portfolioImgs.size());
		
		portfolioImgs.stream().forEach(img -> {
			fileService.deleteFile(portfolioImgLocation + "/" + img.getImgName());
			System.out.println("delete img : " + portfolioImgLocation + "/" + img.getImgName());
			portfolioImgRepo.delete(img);
		});
	}
	
	public List<String> getImgUrls(Long profileId) {
		return portfolioImgRepo.findByPortfolioId(profileId).stream().map(PortfolioImg::getImgUrl).toList();
	}
}
