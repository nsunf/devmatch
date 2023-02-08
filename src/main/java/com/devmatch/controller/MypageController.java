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
import com.devmatch.dto.StackFormDto;
import com.devmatch.service.MemberService;
import com.devmatch.service.StackService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MemberService memberService;
	private final StackService stackService;
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
			System.out.println("editMemberFormDto error");
			bindingResult.getAllErrors().forEach(e -> System.out.println(e));
			return "mypage/editInfo";
		}
		
		MultipartFile profileImgFile = null;
		if (profileImgFileList.size() > 0) profileImgFile = profileImgFileList.get(0);
		
		try {
			memberService.updateMember(editMemberFormDto, profileImgFile, passwordEncoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		memberService.login(memberFormDto);
		
		return "redirect:/mypage/info";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/stacks")
	public String stack(Model model) {
		model.addAttribute("stackFormDto", new StackFormDto());
		model.addAttribute("stackList", stackService.getAdminStackList());
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
