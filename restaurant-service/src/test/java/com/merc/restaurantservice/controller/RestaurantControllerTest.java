package com.merc.restaurantservice.controller;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.merc.restaurantservice.exception.RestaurantServiceException;
import com.merc.restaurantservice.model.Item;
import com.merc.restaurantservice.model.RestaurantData;
import com.merc.restaurantservice.service.RestaurantService;

import reactor.core.publisher.Mono;

@WebFluxTest(RestaurantController.class)
class RestaurantControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;

	@MockBean
	private RestaurantService restaurantService;
	
	@Test
    public void testGetAll() {
		RestaurantData response = new RestaurantData();
		List<Item> items = new ArrayList<>();
		Item item1 = new Item(1000L, "Marriott", true);
		Item item2 = new Item(1000L, "Taj", false);
		Item item3 = new Item(1000L, "Oberoi", true);
		items.add(item1);
		items.add(item2);
		items.add(item3);
		response.setRestaurants(items);
        Mono<RestaurantData> restaurantMono = Mono.just(response);

        when(restaurantService.getAll("Mumbai")).thenReturn(restaurantMono);

        webTestClient.get()
                .uri("/api/v1/restaurant/Mumbai")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RestaurantData.class)
                .value(resp -> resp.getRestaurants().size(), equalTo(3));
    }
	
	@Test
    public void testGetAll_ThrowsException() {
        when(restaurantService.getAll("Mumbai")).thenThrow(new RestaurantServiceException("Error!"));

        webTestClient.get()
                .uri("/api/v1/restaurant/Mumbai")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
