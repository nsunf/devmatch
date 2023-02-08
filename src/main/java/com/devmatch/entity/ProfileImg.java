package com.devmatch.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profile_image")
@Getter
@Setter
public class ProfileImg extends Image {
	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;
}
