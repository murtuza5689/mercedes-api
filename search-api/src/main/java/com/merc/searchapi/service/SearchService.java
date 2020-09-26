package com.merc.searchapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.merc.searchapi.constant.ApiConstants;
import com.merc.searchapi.model.RestaurantData;
import com.merc.searchapi.model.SearchResults;

import reactor.core.publisher.Mono;

@Service
public class SearchService {

	@Value("${restaurant.service.base.url}")
	private String restaurant_service_baseURL;
	
	@Value("${parking-spot.service.base.url}")
	private String parking_spot_service_baseURL;
	
	@Value("${charging-station.service.base.url}")
	private String charging_station_service_baseURL;
	
	@Autowired
	private WebClient.Builder clientBuilder;

	public Mono<SearchResults> search(String location) {
		return searchRestaurants(location);
	}

	private Mono<SearchResults> searchRestaurants(String location) {
		return clientBuilder.baseUrl(restaurant_service_baseURL).build().get()
				.uri(ApiConstants.RESTAURANT_URL + location).retrieve()
				.bodyToFlux(RestaurantData.class)
				.flatMapIterable(RestaurantData::getRestaurants)
				.take(3)
				.collectList()
				.map(restaurants -> new SearchResults(restaurants));
//				.map(restaurants -> new SearchResults(restaurants.getRestaurants()));

	}

}
