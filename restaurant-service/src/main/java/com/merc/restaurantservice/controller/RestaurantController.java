package com.merc.restaurantservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merc.restaurantservice.model.RestaurantData;
import com.merc.restaurantservice.service.RestaurantService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;

	@GetMapping("/v1/restaurant/{loc}")
	public Mono<RestaurantData> getAll(@PathVariable("loc") String loc) {
		return restaurantService.getAll(loc);
	}
	
	
}
