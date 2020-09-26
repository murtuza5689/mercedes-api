package com.merc.searchapi.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.merc.searchapi.constant.ApiConstants;
import com.merc.searchapi.model.Item;
import com.merc.searchapi.model.ResponseData;
import com.merc.searchapi.model.SearchResults;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SearchService {

	final ReactiveCircuitBreaker rcb;

	@Value("${restaurant.service.base.url}")
	private String restaurant_service_baseURL;

	@Value("${parking-spot.service.base.url}")
	private String parking_spot_service_baseURL;

	@Value("${charging-station.service.base.url}")
	private String charging_station_service_baseURL;

	@Autowired
	private WebClient.Builder clientBuilder;

	public SearchService(ReactiveCircuitBreakerFactory rcbf) {
		this.rcb = rcbf.create("search-rcb");
	}

	public Mono<SearchResults> search(String location) {
		Mono<List<Item>> restaurants = searchTop3NearByRestaurants(location);
		Mono<List<Item>> stations = searchTop3NearByChargingStations(location);
		return Mono.zip(restaurants, stations).map(tuple -> new SearchResults(tuple.getT1(), tuple.getT2()));
	}

	private Mono<List<Item>> searchTop3NearByRestaurants(String location) {
		return rcb.run(
				clientBuilder.baseUrl(restaurant_service_baseURL).build().get()
					.uri(ApiConstants.RESTAURANT_URL + location)
					.retrieve()
					.bodyToFlux(ResponseData.class)
					.flatMapIterable(ResponseData::getItems)
					.take(3)
					.collectList()
					.subscribeOn(Schedulers.elastic())
					.retry(3).timeout(Duration.ofSeconds(2), Mono.just(fallbackRestaurantResponse())),
				throwable -> Mono.just(fallbackRestaurantResponse()));

	}

	private Mono<List<Item>> searchTop3NearByChargingStations(String location) {
		return rcb.run(
				clientBuilder.baseUrl(charging_station_service_baseURL).build().get()
					.uri(ApiConstants.GAS_STATION_URL + location)
					.retrieve()
					.bodyToFlux(ResponseData.class)
					.flatMapIterable(ResponseData::getItems)
					.take(3)
					.collectList()
					.subscribeOn(Schedulers.elastic())
					.retry(3).timeout(Duration.ofSeconds(2), Mono.just(fallbackChargingStationResponse())),
				throwable -> Mono.just(fallbackChargingStationResponse()));

	}

	private List<Item> fallbackChargingStationResponse() {
		return Arrays.asList(
				new Item(1L, "Dummy Charging Station 1", true),
				new Item(2L, "Dummy Charging Station 2", true), 
				new Item(3L, "Dummy Charging Station 3", true));
	}

	private List<Item> fallbackRestaurantResponse() {
		return Arrays.asList(
				new Item(1L, "Dummy Restaurant1", true), 
				new Item(2L, "Dummy Restaurant2", true),
				new Item(3L, "Dummy Restaurant3", true));
	}

}
