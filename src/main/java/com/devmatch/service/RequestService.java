package com.devmatch.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.constant.RequestType;
import com.devmatch.dto.MemberDto;
import com.devmatch.dto.RequestDto;
import com.devmatch.dto.RequestFormDto;
import com.devmatch.entity.Member;
import com.devmatch.entity.Request;
import com.devmatch.repository.RequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestService {
	private final RequestRepository requestRepo;
	private final MemberService memberService;
	private final ProfileService profileService;

	public void createRequest(RequestFormDto requestFormDto) {
		Member customer = memberService.getMember();
		Member provider = profileService.getProfile(requestFormDto.getProfileId()).getMember();
		
		Request request = new Request();
		request.setCustomer(customer);
		request.setProvider(provider);
		request.setTitle(requestFormDto.getTitle());
		request.setReward(requestFormDto.getReward());
		request.setDeadline(LocalDate.parse(requestFormDto.getDeadline()));
		request.setContent(requestFormDto.getContent());
		request.setStatus(RequestType.REQUEST);
		request.setAccepted(false);
		
		requestRepo.save(request);
	}
	
	@Transactional(readOnly = true)
	public RequestDto parseToRequestDto(Request request) {
		Member provider = request.getProvider();
		MemberDto providerDto = memberService.getMemberDtoById(provider.getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		RequestDto requestDto = new RequestDto();
		requestDto.setId(request.getId());
		requestDto.setProvider(providerDto);
		requestDto.setTitle(request.getTitle());
		requestDto.setRegDate(request.getRegTime().format(formatter));
		requestDto.setReward(request.getReward());
		requestDto.setDeadline(request.getDeadline().format(formatter));
		requestDto.setContent(request.getContent());
		requestDto.setStatus(request.getStatus());
		requestDto.setAccepted(request.isAccepted());
		
		return requestDto;
	}
	
	@Transactional(readOnly = true)
	public Page<RequestDto> getRequestDtoList(Pageable pageable) {
		Member member = memberService.getMember();

		Page<RequestDto> requestDtoList = requestRepo.findByCustomerId(member.getId(), pageable).map(entity -> {
			return parseToRequestDto(entity);
		});

		return requestDtoList;
	}
	
	@Transactional(readOnly = true)
	public RequestDto getRequestDto(Long id) {
		Request req =  requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		return parseToRequestDto(req);
	}
}
