package com.devmatch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partners")
public class PartnerController {
	
	@GetMapping("")
	public String partners() {
		return "partners/partnerList";
	}
}
