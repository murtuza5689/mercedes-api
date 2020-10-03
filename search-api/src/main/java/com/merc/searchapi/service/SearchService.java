package com.merc.searchapi.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.merc.searchapi.constant.ApiConstants;
import com.merc.searchapi.helper.FallBackHelper;
import com.merc.searchapi.model.DataWrapper;
import com.merc.searchapi.model.ResponseData;
import com.merc.searchapi.model.ResponseItem;
import com.merc.searchapi.model.SearchResults;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SearchService {

	final ReactiveCircuitBreaker rcb;

	@Value("${restaurant.service.base.url}")
	private String restaurant_service_baseURL;

	@Value("${charging-station.service.base.url}")
	private String charging_station_service_baseURL;
	
	@Value("${here.maps.geocode.url}")
	private String geolocation_baseURL;
	
	@Value("${apiKey}")
	private String apiKey;
	
	@Autowired
	private WebClient.Builder clientBuilder;
	
	public SearchService(ReactiveCircuitBreakerFactory rcbf) {
		this.rcb = rcbf.create("search-rcb");
	}

	public Mono<SearchResults> search(String location) {

		return WebClient.builder().build().get()
				.uri(geolocation_baseURL + "?q={location}&apiKey={apiKey}", location, apiKey)
					.retrieve()
					.bodyToFlux(DataWrapper.class)
					.flatMapIterable(DataWrapper::getItems)
					.take(1)
					.collectList()
					.flatMap(i-> {
						String position = new StringBuilder(
							i.get(0).getPosition().getLat() + ","+ 
									i.get(0).getPosition().getLng()).toString();
						Mono<List<ResponseItem>> restaurants = searchTop3NearByRestaurants(position);
						Mono<List<ResponseItem>> stations = searchTop3NearByChargingStations(position);
						return Mono.zip(restaurants, stations)
								.map(tuple -> new SearchResults(tuple.getT1(), tuple.getT2()));
					});
		
	}

	private Mono<List<ResponseItem>> searchTop3NearByRestaurants(String location) {
		return rcb.run(
				clientBuilder.baseUrl(restaurant_service_baseURL).build().get()
					.uri(ApiConstants.RESTAURANT_URL + location)
					.retrieve()
					.bodyToFlux(ResponseData.class)
					.flatMapIterable(ResponseData::getItems)
					.take(3)
					.collectList()
					.subscribeOn(Schedulers.elastic())
					.retry(3).timeout(Duration.ofSeconds(2), Mono.just(FallBackHelper.fallbackRestaurantResponse())),
				throwable -> Mono.just(FallBackHelper.fallbackRestaurantResponse()));

	}

	private Mono<List<ResponseItem>> searchTop3NearByChargingStations(String location) {
		return rcb.run(
				clientBuilder.baseUrl(charging_station_service_baseURL).build().get()
					.uri(ApiConstants.GAS_STATION_URL + location)
					.retrieve()
					.bodyToFlux(ResponseData.class)
					.flatMapIterable(ResponseData::getItems)
					.take(3)
					.collectList()
					.subscribeOn(Schedulers.elastic())
					.retry(3).timeout(Duration.ofSeconds(2), Mono.just(FallBackHelper.fallbackChargingStationResponse())),
				throwable -> Mono.just(FallBackHelper.fallbackChargingStationResponse()));

	}
	
}
