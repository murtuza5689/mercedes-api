package com.merc.searchapi.helper;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.merc.searchapi.model.ResponseItem;

@Component
public class FallBackHelper {

	
	public static List<ResponseItem> fallbackChargingStationResponse() {
		return Arrays.asList(
				new ResponseItem(1L, "Dummy Charging Station 1"),
				new ResponseItem(2L, "Dummy Charging Station 2"), 
				new ResponseItem(3L, "Dummy Charging Station 3"));
	}

	public static List<ResponseItem> fallbackRestaurantResponse() {
		return Arrays.asList(
				new ResponseItem(1L, "Dummy Restaurant1"), 
				new ResponseItem(2L, "Dummy Restaurant2"),
				new ResponseItem(3L, "Dummy Restaurant3"));
	}
	
	public static List<ResponseItem> fallbackParkingSpotResponse() {
		return Arrays.asList(
				new ResponseItem(1L, "Dummy Parking1"), 
				new ResponseItem(2L, "Dummy Parking2"),
				new ResponseItem(3L, "Dummy Parking3"));
	}

}
