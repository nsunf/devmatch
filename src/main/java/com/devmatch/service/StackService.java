package com.devmatch.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devmatch.dto.StackDto;
import com.devmatch.dto.StackFormDto;
import com.devmatch.entity.Stack;
import com.devmatch.entity.StackImg;
import com.devmatch.repository.StackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StackService {
	private final StackRepository stackRepo;
	private final StackImgService stackImgService;
	
	public void saveStack(StackFormDto stackFormDto, MultipartFile stackImgFile) throws IOException {
		Stack stack = stackFormDto.toStack();
		stackRepo.save(stack);
		
		StackImg stackImg = new StackImg();
		stackImg.setStack(stack);
		stackImgService.saveImg(stackImg, stackImgFile);
	}
	
	@Transactional(readOnly = true)
	public List<StackDto> getStackDtoList() {
		List<Stack> stackList = stackRepo.findAll();
		return stackList.stream().map(StackDto::new).toList();
	}
	
	public void updateStack(StackFormDto stackFormDto, MultipartFile stackImgFile) throws IOException {
		Stack stack = stackRepo.findById(stackFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		
		stack.setName(stackFormDto.getName());
		
		stackImgService.updateImg(stack.getId(), stackImgFile);
	}

	public void deleteStack(Long stackId) {
		Stack stack = stackRepo.findById(stackId).orElseThrow(EntityNotFoundException::new);
		stackRepo.delete(stack);
	}
}
