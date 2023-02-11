package com.devmatch.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.EditMemberFormDto;
import com.devmatch.dto.ProfileFormDto;
import com.devmatch.dto.StackDto;
import com.devmatch.dto.StackFormDto;
import com.devmatch.entity.Member;
import com.devmatch.service.MemberService;
import com.devmatch.service.ProfileService;
import com.devmatch.service.StackService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MemberService memberService;
	private final StackService stackService;
	private final ProfileService profileService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/dashboard")
	public String admin(Model model, Principal principal) {
		model.addAttribute("memberDto", memberService.getMemberDto(principal.getName()));

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
		
		profileImgFileList.stream().forEach(f -> System.out.println(f.getOriginalFilename()));
		
		try {
			Member member = memberService.getMember();
			profileService.updateProfile(member, profileFormDto, profileImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/mypage/dashboard";
		}

		return "redirect:/mypage/profile";
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
