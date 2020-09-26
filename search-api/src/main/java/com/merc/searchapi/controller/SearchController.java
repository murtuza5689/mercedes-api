package com.merc.searchapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merc.searchapi.model.SearchResults;
import com.merc.searchapi.service.SearchService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@GetMapping("/search/{loc}")
	public Mono<SearchResults> search(@PathVariable("loc") String loc) {
		return searchService.search(loc);
	}
}
