package com.devmatch.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "portfolio_img")
@Entity
public class PortfolioImg extends Image {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name =  "portfolio_id")
	private Portfolio portfolio;
}
