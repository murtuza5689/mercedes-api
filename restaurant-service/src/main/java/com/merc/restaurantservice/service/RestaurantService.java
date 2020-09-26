package com.merc.restaurantservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.merc.restaurantservice.exception.RestaurantServiceException;
import com.merc.restaurantservice.model.Item;
import com.merc.restaurantservice.model.ResponseWrapper;
import com.merc.restaurantservice.model.RestaurantData;

import reactor.core.publisher.Mono;

@Service
public class RestaurantService {

	final ReactiveCircuitBreaker rcb;

	@Value("${category.type}")
	private String categoryType;

	@Value("${apiKey}")
	private String apiKey;

	@Autowired
	private WebClient client;

	public RestaurantService(ReactiveCircuitBreakerFactory rcbf) {
		this.rcb = rcbf.create("restaurant-rcb");
	}

	public Mono<RestaurantData> getAll(String location) {
		return rcb.run(client.get()
				.uri("?at={location}&cat={categoryType}&apiKey={apiKey}", location, categoryType, apiKey)
				.retrieve()
				.onStatus(HttpStatus::is5xxServerError,
						response -> Mono.just(new RestaurantServiceException("Internal error!")))
				.onStatus(HttpStatus::is4xxClientError,
						response -> Mono.just(new RestaurantServiceException("Client Error!")))
				.onStatus(HttpStatus::isError, response -> Mono.just(new RestaurantServiceException("Error!")))
				.bodyToFlux(ResponseWrapper.class)
				.map(ResponseWrapper::getResults)
				.flatMapIterable(RestaurantData::getRestaurants)
				.collectSortedList(
						(restaurant1, restaurant2) -> restaurant1.getDistance().compareTo(restaurant2.getDistance()))
				.map(restaurant -> new RestaurantData(restaurant)).cache(), throwable -> {
					return Mono.just(
							new RestaurantData(Collections.singletonList(new Item(0L, "Dummy Restaurant", true))));
				});
	}

}
