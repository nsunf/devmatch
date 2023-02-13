package com.devmatch.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.PortfolioDto;
import com.devmatch.dto.PortfolioFormDto;
import com.devmatch.entity.Member;
import com.devmatch.entity.Portfolio;
import com.devmatch.entity.PortfolioImg;
import com.devmatch.entity.Profile;
import com.devmatch.repository.PortfolioRepository;
import com.devmatch.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioService {
	
	private final PortfolioRepository portfolioRepo;
	private final ProfileRepository profileRepo;
	private final MemberService memberService;
	private final PortfolioImgService portfolioImgService;
	
	public void savePortfolio(PortfolioFormDto portfolioFormDto, List<MultipartFile> portfolioImgFileList) throws IOException {
		Member member = memberService.getMember();
		Portfolio portfolio = null;
		if (portfolioFormDto.getId() == null) {
			portfolio = new Portfolio();
			portfolioRepo.save(portfolio);
		} else {
			portfolio = portfolioRepo.findById(portfolioFormDto.getId()).get();
		}
		Profile profile = profileRepo.findByMemberId(member.getId()).orElseThrow(EntityNotFoundException::new);

		portfolio.update(portfolioFormDto.getTitle(), portfolioFormDto.getContent(), profile, member);
		
		if (!portfolioImgFileList.isEmpty() && !portfolioImgFileList.get(0).isEmpty()) {
			portfolioImgService.deleteImgs(portfolio.getId());
			for (MultipartFile portfolioImgFile: portfolioImgFileList) {
				PortfolioImg portfolioImg = new PortfolioImg();
				portfolioImg.setPortfolio(portfolio);
				portfolioImgService.saveImg(portfolioImg, portfolioImgFile);
			}
		}
	}
	
	public PortfolioDto getPortfolio(Long portfolioId) {
		Portfolio portfolio = portfolioRepo.findById(portfolioId).orElseThrow(EntityNotFoundException::new);
		
		List<String> imgUrlList = portfolioImgService.getImgUrls(portfolioId);
		
		return new PortfolioDto(portfolio, imgUrlList);
	}

	public List<PortfolioDto> getPortfolioList() {
		Member member = memberService.getMember();
		List<Portfolio> portfolioList = portfolioRepo.findByMemberId(member.getId());
		
		List<PortfolioDto> portfolioDtoList = portfolioList.stream().map(portfolio -> {
			List<String> portfolioImgUrlList = portfolioImgService.getImgUrls(portfolio.getId());
			return new PortfolioDto(portfolio, portfolioImgUrlList);
		}).toList();

		return portfolioDtoList;
	}
	
	public void deletePortfolio(Long portfolioId) {
		portfolioImgService.deleteImgs(portfolioId);
		portfolioRepo.deleteById(portfolioId);
	}
}
