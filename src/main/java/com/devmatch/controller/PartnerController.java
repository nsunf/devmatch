package com.devmatch.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devmatch.dto.PartnerSearchDto;
import com.devmatch.dto.ProfileCardDto;
import com.devmatch.service.ProfileService;
import com.devmatch.service.StackService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerController {
	
	private final ProfileService profileService;
	private final StackService stackService; 
	
	@GetMapping("")
	public String partners(@RequestParam(required = false) String searchQuery, @RequestParam(required = false) List<Long> stackIdList, Model model) {
		PartnerSearchDto partnerSearchDto = new PartnerSearchDto();

		if (searchQuery != null && stackIdList != null) {
			partnerSearchDto.setSearchQuery(searchQuery);
			partnerSearchDto.setStackIdList(stackIdList);
		}

		model.addAttribute("partnerSearchDto", partnerSearchDto);
		model.addAttribute("stackList", stackService.getStackDtoListAll());
		
		return "partners/partnerList";
	}
	
	@GetMapping("/load")
	public @ResponseBody ResponseEntity<?> loadPartners(@RequestParam(required = false) String searchQuery, @RequestParam(required = false) List<Long> stackIdList , @RequestParam Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.orElse(0), 8);
		
		Page<ProfileCardDto> profileCardDtoList = profileService.getProfileCardDtoList(searchQuery, stackIdList, pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("result", profileCardDtoList);
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
	}
	
	@GetMapping("/{partnerId}")
	public String partnerProfile(@PathVariable Long partnerId, Model model) {
		
		model.addAttribute("profileDto", profileService.getProfileDto(partnerId));
		return "partners/profile";
	}
}
