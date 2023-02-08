package com.devmatch.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stack_image")
@Getter
@Setter
public class StackImg extends Image {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stack_id")
	private Stack stack;
	
	public void update(String imgName, String imgUrl, String oriImgName) {
		setImgName(imgName);
		setImgUrl(imgUrl);
		setOriImgName(oriImgName);
	}
}
