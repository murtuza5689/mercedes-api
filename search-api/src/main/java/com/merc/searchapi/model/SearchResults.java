package com.merc.searchapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResults {

	@JsonProperty("restaurants")
	private List<Item> restaurants;
	
	@JsonProperty("charging-stations")
	private List<Item> stations;
	
	public SearchResults() {}
	
	public SearchResults(List<Item> restaurants) {
		this.restaurants = restaurants;
	}
	
	public SearchResults(List<Item> restaurants, List<Item> stations) {
		this.restaurants = restaurants;
		this.stations = stations;
	}

	public List<Item> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(List<Item> restaurants) {
		this.restaurants = restaurants;
	}

	public List<Item> getStations() {
		return stations;
	}

	public void setStations(List<Item> stations) {
		this.stations = stations;
	}
	
	
	
}
