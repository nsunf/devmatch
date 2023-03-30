package com.devmatch.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmatch.constant.MemberRole;
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
		
		if (request.getTitleTmp() != null)
			requestDto.setTitleTmp(request.getTitleTmp());
		if (request.getRewardTmp() != null)
			requestDto.setRewardTmp(request.getRewardTmp());
		if (request.getDeadlineTmp() != null)
			requestDto.setDeadlineTmp(request.getDeadlineTmp().format(formatter));
		if (request.getContentTmp() != null)
			requestDto.setContentTmp(request.getContentTmp());
		
		return requestDto;
	}
	
	@Transactional(readOnly = true)
	public Page<RequestDto> getRequestDtoList(Pageable pageable) {
		Member member = memberService.getMember();
		Page<RequestDto> requestDtoList = null;
		if (member.getRole() == MemberRole.CUSTOMER)
			requestDtoList = requestRepo.findByCustomerIdOrderByRegTimeDesc(member.getId(), pageable).map(e -> parseToRequestDto(e));
		else 
			requestDtoList = requestRepo.findByProviderIdOrderByRegTimeDesc(member.getId(), pageable).map(e -> parseToRequestDto(e));

		return requestDtoList;
	}
	
	@Transactional(readOnly = true)
	public RequestDto getRequestDto(Long id) {
		Request req =  requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		return parseToRequestDto(req);
	}
	
	@Transactional(readOnly = true)
	public RequestFormDto getRequestFormDto(Long id) {
		Request request = requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		return new RequestFormDto(request);
	}
	
	public void updateRequest(RequestFormDto requestFormDto) throws Exception {
		Request request = requestRepo.findById(requestFormDto.getId()).orElseThrow(EntityNotFoundException::new);

		switch(request.getStatus()) {
		case REQUEST:
			break;
		case PROGRESS: 
			break;
		case COMPLETE:
		case CANCEL:
		case EDIT:
			throw new Exception("수정이 불가능한 상태입니다.");
		}
		
		request.setStatus(RequestType.EDIT);

		request.setTitleTmp(requestFormDto.getTitle());
		request.setRewardTmp(requestFormDto.getReward());
		request.setDeadlineTmp(LocalDate.parse(requestFormDto.getDeadline()));
		request.setContentTmp(requestFormDto.getContent());
		request.setAccepted(false);
	}
	
	public void acceptRequest(Long id) {
		Request request = requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		RequestType reqType = request.getStatus();
		boolean reqAccepted = request.isAccepted();

		if (reqAccepted) return;
		
		if (reqType == RequestType.REQUEST) {
			request.setStatus(RequestType.PROGRESS);
			request.setAccepted(true);
		} else if (reqType == RequestType.EDIT) {
			request.setStatus(RequestType.PROGRESS);
			request.adjustTmp();
			request.setAccepted(true);
		} else if (reqType == RequestType.COMPLETE) {
			// 완료 시 등급 포인트
			request.setAccepted(true);
		} else if (reqType == RequestType.CANCEL) {
			request.setAccepted(true);
		}
			return;
	}
	
	public void revertRequest(Long id) {
		Request request = requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		if (request.isAccepted()) return;

		switch (request.getStatus()) {
		case REQUEST:
			request.setStatus(RequestType.CANCEL);
			request.setAccepted(true);
			break;
		case PROGRESS:
			break;
		case COMPLETE:
		case CANCEL:
		case EDIT:
			request.revert();
			request.setStatus(RequestType.PROGRESS);
			break;
		}
	}
	
	public void denyRequest(Long id) {
		Request request = requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		request.setStatus(RequestType.CANCEL);
		request.revert();
		request.setAccepted(true);
	}
	
	public void cancelRequest(Long id) {
		Request request = requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		request.setStatus(RequestType.CANCEL);
		request.setAccepted(false);
	}

	public void completeRequest(Long id) {
		Request request = requestRepo.findById(id).orElseThrow(EntityNotFoundException::new);
		if (request.getStatus() == RequestType.COMPLETE && !request.isAccepted()) {
			request.setStatus(RequestType.COMPLETE);
			request.setAccepted(true);
		} else {
			request.setStatus(RequestType.COMPLETE);
			request.setAccepted(false);
		}
	}
	
	public Long getCompletedRequestCount() {
		Member member = memberService.getMember();
		return requestRepo.countByCustomerIdAndStatusAndAccepted(member.getId(), RequestType.COMPLETE, true);
	}
	
	public Long getProgressingRequestCount() {
		Member member = memberService.getMember();
		if (member.getRole() == MemberRole.PROVIDER)
			return requestRepo.countByProviderIdAndStatusAndAccepted(member.getId(), RequestType.PROGRESS, true);
		else
			return requestRepo.countByCustomerIdAndStatusAndAccepted(member.getId(), RequestType.PROGRESS, true);
	}
	
	public Integer getSuccessRate() {
		Member member = memberService.getMember();
		Long comp = requestRepo.countByProviderIdAndStatusAndAccepted(member.getId(), RequestType.COMPLETE, true);
		Long canc = requestRepo.countByProviderIdAndStatusAndAccepted(member.getId(), RequestType.CANCEL, true);
		Double result = (comp.doubleValue() / (comp + canc)) * 100;
		return result.intValue();
	}
	
	public Long getTotalRequest() {
		return requestRepo.count();
	}
}
