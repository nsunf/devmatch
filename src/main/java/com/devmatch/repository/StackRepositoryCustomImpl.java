package com.devmatch.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.devmatch.dto.QStackDto;
import com.devmatch.dto.StackDto;
import com.devmatch.entity.QStack;
import com.devmatch.entity.QStackImg;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class StackRepositoryCustomImpl implements StackRepositoryCustom {
	private JPAQueryFactory qf;
	
	public StackRepositoryCustomImpl(EntityManager em) {
		this.qf = new JPAQueryFactory(em);
	}

	@Override
	public List<StackDto> getAdminStackList() {
		QStack stack = QStack.stack;
		QStackImg stackImg = QStackImg.stackImg;
		
		List<StackDto> contents = qf
				.select(new QStackDto(stack.id, stack.name, stackImg.imgUrl))
				.from(stackImg)
				.join(stackImg.stack, stack)
				.orderBy(stackImg.oriImgName.asc())
				.fetch();
		
		return contents;
	}
}
