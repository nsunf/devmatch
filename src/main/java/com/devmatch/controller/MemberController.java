package com.devmatch.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devmatch.dto.MemberFormDto;
import com.devmatch.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String loginPage(Principal principal) {
		System.out.println(principal);
		if (principal != null) return "redirect:/";
		return "auth/login";
	}
	
	@GetMapping("/signup")
	public String signupPage(Model model, Principal principal) {
		if (principal != null) return "redirect:/";
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "auth/signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			System.out.println("memberFormDto error");
			bindingResult.getAllErrors().forEach(e -> System.out.println(e));
			return "auth/signup";
		}
		
		try {
			this.memberService.signup(memberFormDto, passwordEncoder);
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			model.addAttribute("errorMessage", e.getMessage());
			return "auth/signup";
		}
		
		memberService.login(memberFormDto);
		
		return "redirect:/auth/signup/success";
	}
	
	@GetMapping("/signup/success")
	public String signupSuccess() {
		return "auth/signupSuccess";
	}
	
	@GetMapping("/login/error")
	public String loginError(Model model) {
		return "auth/login";
	}
}
