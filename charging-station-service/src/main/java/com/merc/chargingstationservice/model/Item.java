package com.merc.chargingstationservice.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	private Long distance;
	
	@JsonAlias("name")
	private String title;
	private boolean isOpen;
	
}