package com.devmatch.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemberImg extends Image {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	Member member;
}
