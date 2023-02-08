package com.devmatch.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.EditMemberFormDto;
import com.devmatch.dto.MemberDto;
import com.devmatch.dto.MemberFormDto;
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
	
	public void login(MemberFormDto memberFormDto) {
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority("ROLE_" + memberFormDto.getRole().toString()));

		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(
						memberFormDto.getEmail(), 
						memberFormDto.getPassword(), 
						list);
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	public void signup(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		Member member = memberFormDto.toEntity(passwordEncoder);
		
		validateDuplicate(member);
		memberRepo.save(member);
	}
	
	public MemberDto getMemberDto(String email) {
		Member member = memberRepo.findByEmail(email);
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
	
	public void updateMember(EditMemberFormDto editMemberFormDto, MultipartFile profileImgFile, PasswordEncoder passwordEncoder) throws IOException {
		Member member = memberRepo.findByEmail(editMemberFormDto.getEmail());
		
		editMemberFormDto.updateMember(member, passwordEncoder);
		
		MemberImg memberImg = memberImgService.getImg(member.getId());
		
		memberImg.setMember(member);
		
		memberImgService.saveImg(memberImg, profileImgFile);
		
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepo.findByEmail(email);

		if (member == null) throw new UsernameNotFoundException(email);
		
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
}
