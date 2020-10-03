package com.merc.searchapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResults {

	@JsonProperty("restaurants")
	private List<ResponseItem> restaurants;
	
	@JsonProperty("charging-stations")
	private List<ResponseItem> stations;
	
	@JsonProperty("parking-spots")
	private List<ResponseItem> parkingSpots;
	
	public SearchResults() {}
	
	public SearchResults(List<ResponseItem> restaurants) {
		this.restaurants = restaurants;
	}
	
	public SearchResults(List<ResponseItem> restaurants, List<ResponseItem> stations) {
		this.restaurants = restaurants;
		this.stations = stations;
	}
	
	public SearchResults(List<ResponseItem> restaurants, List<ResponseItem> stations, List<ResponseItem> parkingSpots) {
		this.restaurants = restaurants;
		this.stations = stations;
		this.parkingSpots = parkingSpots;
	}

	public List<ResponseItem> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(List<ResponseItem> restaurants) {
		this.restaurants = restaurants;
	}

	public List<ResponseItem> getStations() {
		return stations;
	}

	public void setStations(List<ResponseItem> stations) {
		this.stations = stations;
	}

	public List<ResponseItem> getParkingSpots() {
		return parkingSpots;
	}

	public void setParkingSpots(List<ResponseItem> parkingSpots) {
		this.parkingSpots = parkingSpots;
	}
	
	
}
