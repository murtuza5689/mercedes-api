package com.merc.chargingstationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merc.chargingstationservice.model.ChargingStationData;
import com.merc.chargingstationservice.service.ChargingStationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ChargingStationController {
	
	@Autowired
	private ChargingStationService chargingStationService;

	@GetMapping("/v1/station/{loc}")
	public Mono<ChargingStationData> getAll(@PathVariable("loc") String loc) {
		return chargingStationService.getAll(loc);
	}

}
