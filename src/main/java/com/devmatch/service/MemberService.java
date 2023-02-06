package com.devmatch.service;


import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devmatch.dto.MemberFormDto;
import com.devmatch.entity.Member;
import com.devmatch.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
	private final MemberRepository memberRepo;
	
	public void signup(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		Member member = memberFormDto.toEntity(passwordEncoder);
		
		validateDuplicate(member);
		memberRepo.save(member);
	}
	
	private void validateDuplicate(Member member) {
		Member registeredMember = memberRepo.findByEmail(member.getEmail());
		
		if (registeredMember != null)
			throw new IllegalStateException("이미 가입된 회원입니다.");
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
