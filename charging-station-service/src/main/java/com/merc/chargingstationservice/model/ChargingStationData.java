package com.merc.chargingstationservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingStationData {

	@JsonProperty("items")
	@JsonAlias("chargingStations")
	private List<Item> chargingStations;
}
