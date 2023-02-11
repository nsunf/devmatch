package com.devmatch.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartnerSearchDto {

	private String searchQuery;
	
	private List<Long> stackIdList = new ArrayList<>();
}
