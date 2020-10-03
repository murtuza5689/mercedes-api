package com.merc.searchapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	private Long distance;
	@JsonProperty("title")
	@JsonAlias("name")
	private String name;
	private Position position;
	
	public Item(Long distance, String name) {
		this.distance = distance;
		this.name = name;
	}

}
