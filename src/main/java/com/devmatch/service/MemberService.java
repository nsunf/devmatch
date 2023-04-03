package com.devmatch.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.config.CustomUser;
import com.devmatch.dto.EditMemberFormDto;
import com.devmatch.dto.MemberDto;
import com.devmatch.dto.MemberFormDto;
import com.devmatch.entity.Grade;
import com.devmatch.entity.Member;
import com.devmatch.entity.MemberImg;
import com.devmatch.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
	private final MemberRepository memberRepo;
	private final MemberImgService memberImgService;
	private final GradeService gradeService;
	
	public Member getMember() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepo.findByEmail(email);

		return member; 
	}
	
	public void login(MemberFormDto memberFormDto) {
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority("ROLE_" + memberFormDto.getRole().toString()));
		
		UserDetails userDetails = loadUserByUsername(memberFormDto.getEmail());
		
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(userDetails, memberFormDto.getPassword(), list);
		
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	public void signup(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		Member member = memberFormDto.toEntity(passwordEncoder);
		validateDuplicate(member);

		Grade grade = gradeService.getInitialGrade();
		member.setGrade(grade);
		
		memberRepo.save(member);
	}
	
	public MemberDto getMemberDtoByEmail(String email) {
		Member member = memberRepo.findByEmail(email);
		return new MemberDto(member);
	}

	public MemberDto getMemberDtoById(Long id) {
		Member member = memberRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		return new MemberDto(member);
	}
	
	public EditMemberFormDto getEditMemberFormDto(String email) {
		Member member = memberRepo.findByEmail(email);
		MemberImg memberImg = memberImgService.getImg(member.getId());
		return new EditMemberFormDto(member, memberImg);
	}
	
	private void validateDuplicate(Member member) {
		Member findByEmail = memberRepo.findByEmail(member.getEmail());
		Member findBySsn = memberRepo.findBySsn(member.getSsn());
		
		if (findByEmail != null || findBySsn != null)
			throw new IllegalStateException("이미 가입된 회원입니다.");
	}
	
	public void updateMember(EditMemberFormDto editMemberFormDto, MultipartFile profileImgFile, PasswordEncoder passwordEncoder) throws IOException, UsernameNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		String email = auth.getName();
		Member member = memberRepo.findByEmail(email);
		
		if (member == null) throw new UsernameNotFoundException(email);
		
		editMemberFormDto.updateMember(member, passwordEncoder);
		
		MemberImg memberImg = memberImgService.getImg(member.getId());
		
		memberImg.setMember(member);
		
		memberImgService.saveImg(memberImg, profileImgFile);

		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		CustomUser customUser = (CustomUser) userDetails;
		customUser.setMemberImgUrl(memberImg.getImgUrl());
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepo.findByEmail(email);

		if (member == null) throw new UsernameNotFoundException(email);
		
		MemberImg memberImg = memberImgService.getImg(member.getId());
		
		CustomUser customUser = new CustomUser();
		customUser.setUsername(member.getEmail());
		customUser.setPassword(member.getPassword());
		customUser.setEnabled(true);
		customUser.setAccountNonExpired(true);
		customUser.setAccountNonLocked(true);
		customUser.setCredentialsNonExpired(true);
		customUser.setMemberImgUrl(memberImg.getImgUrl());
		
		List<GrantedAuthority> authList = new ArrayList<>();
		authList.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));
		
		customUser.setAuthorities(authList);
		
		return customUser;
		
//		return User.builder()
//				.username(member.getEmail())
//				.password(member.getPassword())
//				.roles(member.getRole().toString())
//				.build();
	}
}
