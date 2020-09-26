package com.merc.chargingstationservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.merc.chargingstationservice.exception.ChargingStationServiceException;
import com.merc.chargingstationservice.model.ChargingStationData;
import com.merc.chargingstationservice.model.Item;
import com.merc.chargingstationservice.model.ResponseWrapper;

import reactor.core.publisher.Mono;

@Service
public class ChargingStationService {

	final ReactiveCircuitBreaker rcb;

	@Value("${category.type}")
	private String categoryType;

	@Value("${apiKey}")
	private String apiKey;

	@Autowired
	private WebClient client;

	public ChargingStationService(ReactiveCircuitBreakerFactory rcbf) {
		this.rcb = rcbf.create("charging-station-rcb");
	}

	public Mono<ChargingStationData> getAll(String location) {
		return rcb.run(
				client.get().uri("?at={location}&cat={categoryType}&apiKey={apiKey}", location, categoryType, apiKey)
						.retrieve()
						.onStatus(HttpStatus::is5xxServerError,
								response -> Mono.just(new ChargingStationServiceException("Internal error!")))
						.onStatus(HttpStatus::is4xxClientError,
								response -> Mono.just(new ChargingStationServiceException("Client Error!")))
						.onStatus(HttpStatus::isError,
								response -> Mono.just(new ChargingStationServiceException("Error!")))
						.bodyToFlux(ResponseWrapper.class).map(ResponseWrapper::getResults)
						.flatMapIterable(ChargingStationData::getChargingStations)
						.collectSortedList(
								(station1, station2) -> station1.getDistance().compareTo(station1.getDistance()))
						.map(restaurant -> new ChargingStationData(restaurant)).cache(),
				throwable -> Mono.just(new ChargingStationData(
						Collections.singletonList(new Item(0L, "Dummy Charging Station", true)))));
	}
}
