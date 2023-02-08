package com.devmatch.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Image extends BaseEntity {
	
	@Id
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName;
	
	private String imgUrl;
	
	private String oriImgName;
	
	public void updateImage(String imgName, String imgUrl, String oriImgName) {
		this.imgName = imgName;
		this.imgUrl = imgUrl;
		this.oriImgName = oriImgName;
	}
}
