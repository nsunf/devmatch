package com.devmatch.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.EditMemberFormDto;
import com.devmatch.dto.PortfolioDto;
import com.devmatch.dto.PortfolioFormDto;
import com.devmatch.dto.ProfileFormDto;
import com.devmatch.dto.RequestFormDto;
import com.devmatch.dto.StackDto;
import com.devmatch.dto.StackFormDto;
import com.devmatch.entity.Member;
import com.devmatch.service.MemberService;
import com.devmatch.service.PortfolioService;
import com.devmatch.service.ProfileService;
import com.devmatch.service.RequestService;
import com.devmatch.service.StackService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MemberService memberService;
	private final StackService stackService;
	private final ProfileService profileService;
	private final PortfolioService portfolioService;
	private final RequestService requestService;

	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/dashboard")
	public String admin(Model model, Principal principal) {
		model.addAttribute("memberDto", memberService.getMemberDtoByEmail(principal.getName()));

		return "mypage/dashboard";
	}


	@GetMapping("/info")
	public String info(Model model, Principal principal) {
		String email = principal.getName();
		EditMemberFormDto emfd = memberService.getEditMemberFormDto(email);
		
		model.addAttribute("memberFormDto", emfd);
		return "mypage/editInfo";
	}
	
	@PostMapping("/info")
	public String updateInfo(@Valid EditMemberFormDto editMemberFormDto, BindingResult bindingResult, List<MultipartFile> profileImgFileList, Model model) {
		if (bindingResult.hasErrors()) {
			return "mypage/editInfo";
		}
		
		try {
			memberService.updateMember(editMemberFormDto, profileImgFileList.get(0), passwordEncoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/mypage/info";
	}
	
	@GetMapping("/requests")
	@PreAuthorize("hasAnyRole('ROLE_PROVIDER', 'ROLE_CUSTOMER')")
	public String requestList(@RequestParam Optional<Integer> page, Model model) {
		// 역할에 따라 다름
		Pageable pageable = PageRequest.of(page.orElse(0), 3);
		model.addAttribute("requestDtoList", requestService.getRequestDtoList(pageable));
		model.addAttribute("page", pageable.getPageNumber());
		model.addAttribute("maxPage", 5);
		return "mypage/requestList";
	}
	
	@GetMapping("/requests/{id}")
	public String requestDetail(@PathVariable Long id, Model model) {
		model.addAttribute("requestDto", requestService.getRequestDto(id));
		return "mypage/requestDetail";
	}
	
	@GetMapping("/requests/edit/{id}")
	public String editRequest(@PathVariable Long id, Model model) {
		model.addAttribute("requestFormDto", requestService.getRequestFormDto(id));
		return "mypage/editRequest";
	}
	
	@PostMapping("/requests/edit/{id}")
	public String editRequest(@PathVariable Long id, @Valid RequestFormDto requestFormDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("requestFormDto", requestService.getRequestFormDto(id));
			return "mypage/editRequest";
		}
		
		try {
			requestService.updateRequest(requestFormDto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/mypage/requests/" + id;
	}
	
	@GetMapping("/requests/revert/{id}")
	public String revertRequest(@PathVariable Long id) {
		requestService.revertRequest(id);
		return "redirect:/mypage/requests/" + id;
	}
	
	@GetMapping("/requests/cancel/{id}")
	public String cancelRequest(@PathVariable Long id) {
		requestService.cancelRequest(id);
		return "redirect:/mypage/requests/" + id;
	}
	
	
//	PROVIDER
	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/profile")
	public String profile(Model model) {
		Member member = memberService.getMember();
		ProfileFormDto profileFormDto = profileService.getProfileFormDto(member.getId());
//		List<ProfileStackDto> profileStackDtoList = stackService.getProfileStackDtoList(profileFormDto.getId());
		List<StackDto> stackDtoList = stackService.getStackDtoListAll();
		List<Long> checkedStackIdList = stackService.getStackDtoListByProfileId(profileFormDto.getId()).stream().map(StackDto::getId).toList();
		
		profileFormDto.setStackIdList(checkedStackIdList);
//		profileFormDto.setProfileStackDtoList(profileStackDtoList);

		System.out.println("===> " + profileFormDto);
		model.addAttribute("profileFormDto", profileFormDto);
		model.addAttribute("stackDtoList", stackDtoList);
		return "mypage/editProfile";
	}
	
	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@PostMapping("/profile")
	public String updateProfile(@Valid ProfileFormDto profileFormDto, BindingResult bindingResult, List<MultipartFile> profileImgFileList) {
		
		if (bindingResult.hasErrors()) {
			return "mypage/editProfile";
		}
		
		try {
			Member member = memberService.getMember();
			profileService.updateProfile(member, profileFormDto, profileImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/mypage/dashboard";
		}

		return "redirect:/mypage/profile";
	}
	
	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/portfolios")
	public String portfolios(Model model) {
		model.addAttribute("portfolioDtoList", portfolioService.getPortfolioListByMember());

		return "mypage/editPortfolioList";
	}

	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/portfolios/load/{portfolioId}")
	public @ResponseBody ResponseEntity<?> portfolioDetail(@PathVariable Long portfolioId) {
		PortfolioDto portfolioDto = portfolioService.getPortfolio(portfolioId);
		
		Map<String, PortfolioDto> map = new HashMap<>();
		map.put("result", portfolioDto);
		
		return new ResponseEntity<Map<String, PortfolioDto>>(map, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/portfolios/edit")
	public String editPortfolio(Model model) {
		if (!profileService.isExist()) {
			model.addAttribute("errorMessage", "프로필을 먼저 등록해주세요.");
		}
		
		model.addAttribute("portfolioDto", new PortfolioDto());
		return "mypage/editPortfolio";
	}

	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@PostMapping("/portfolios/edit")
	public String updatePortfolio(PortfolioFormDto portfolioFormDto, List<MultipartFile> portfolioImgFileList, Model model) {
		try {
			portfolioService.savePortfolio(portfolioFormDto, portfolioImgFileList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/mypage/portfolios";
	}

	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/portfolios/edit/{portfolioId}")
	public String editPortfolioById(@PathVariable Long portfolioId, Model model) {
		PortfolioDto portfolioDto = portfolioService.getPortfolio(portfolioId);
		
		model.addAttribute("portfolioDto", portfolioDto);
		return "mypage/editPortfolio";
	}

	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/portfolios/delete/{portfolioId}")
	public String deletePortfolioById(@PathVariable Long portfolioId, Model model) {
		portfolioService.deletePortfolio(portfolioId);

		return "redirect:/mypage/portfolios";
	}
	
	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/requests/accept/{id}")
	public String acceptRequest(@PathVariable Long id) {
		requestService.acceptRequest(id);
		return "redirect:/mypage/requests/" + id;
	}
	
	@PreAuthorize("hasRole('ROLE_PROVIDER')")
	@GetMapping("/requests/deny/{id}")
	public String denyRequest(@PathVariable Long id) {
		requestService.denyRequest(id);
		return "redirect:/mypage/requests/" + id;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_PROVIDER', 'ROLE_CUSTOMER')")
	@GetMapping("/requests/complete/{id}")
	public String completeRequest(@PathVariable Long id) {
		requestService.completeRequest(id);
		return "redirect:/mypage/requests/" + id;
	}
	
	
//	ADMIN
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/stacks")
	public String stack(Model model) {
		model.addAttribute("stackFormDto", new StackFormDto());
		model.addAttribute("stackList", stackService.getStackDtoListAll());
		return "mypage/stackList";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/stacks/add")
	public String addStack(StackFormDto stackFormDto, @RequestParam("stackImgFile") List<MultipartFile> stackImgFileList) {
		MultipartFile stackImg = null;
		if (stackImgFileList.size() > 0) {
			stackImg = stackImgFileList.get(0);
		}
		
		try {
			stackService.saveStack(stackFormDto, stackImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/mypage/stacks";
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/stacks/update")
	public String updateStack(StackFormDto stackFormDto, @RequestParam("stackImgFile") List<MultipartFile> stackImgFileList) {
		MultipartFile stackImg = null;
		if (stackImgFileList.size() > 0) stackImg = stackImgFileList.get(0);

		try {
			stackService.updateStack(stackFormDto, stackImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/mypage/stacks";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/stacks/delete")
	public String deleteStack(@RequestParam("id") Long id) {
		stackService.deleteStack(id);
		return "redirect:/mypage/stacks";
	}
}
