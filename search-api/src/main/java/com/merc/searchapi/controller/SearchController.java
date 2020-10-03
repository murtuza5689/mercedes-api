package com.merc.searchapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.merc.searchapi.model.SearchResults;
import com.merc.searchapi.service.SearchService;

import reactor.core.publisher.Mono;

@RestController
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@GetMapping("/v1/search")
	public Mono<SearchResults> search(@RequestParam("q") String loc) {
		return searchService.search(loc);
	}
}
