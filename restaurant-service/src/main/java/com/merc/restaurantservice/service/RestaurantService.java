package com.merc.restaurantservice.service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.merc.restaurantservice.exception.RestaurantServiceException;
import com.merc.restaurantservice.model.Restaurant;
import com.merc.restaurantservice.model.RestaurantData;
import com.merc.restaurantservice.model.RestaurantWrapper;

import reactor.core.publisher.Mono;

@Service
public class RestaurantService {

	@Value("${category.type}")
	private String categoryType;

	@Value("${apiKey}")
	private String apiKey;

	@Autowired
	private WebClient client;

	public Mono<RestaurantData> search(String location) {
		Mono<List<Restaurant>> restaurants = client.get()
				.uri("?at={location}&cat={categoryType}&apiKey={apiKey}", location, categoryType,
						apiKey)
				.retrieve()
				.onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new RestaurantServiceException("Internal error!")))
				.onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new RestaurantServiceException("Client Error!")))
				.onStatus(HttpStatus::isError, response -> Mono.just(new RestaurantServiceException("Error!")))
				.bodyToFlux(RestaurantWrapper.class)
				.map(RestaurantWrapper::getResults)
				.flatMapIterable(RestaurantData::getRestaurants)
				.collectSortedList(
						(restaurant1, restaurant2) -> restaurant1.getDistance().compareTo(restaurant2.getDistance()));
		return restaurants.map(restaurant -> new RestaurantData(restaurant))
				.retry(3)
			    .timeout(
			    		Duration.ofSeconds(2), 
			    		Mono.just(
			    				new RestaurantData(Collections.singletonList(
			    						new Restaurant("1", 0L, "Dummy Restaurant", true)))));
	}
	
}
