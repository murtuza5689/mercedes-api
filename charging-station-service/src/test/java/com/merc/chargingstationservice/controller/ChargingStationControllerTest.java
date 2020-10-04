package com.merc.chargingstationservice.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.merc.chargingstationservice.exception.ChargingStationServiceException;
import com.merc.chargingstationservice.model.ChargingStationData;
import com.merc.chargingstationservice.model.Item;
import com.merc.chargingstationservice.service.ChargingStationService;

import reactor.core.publisher.Mono;

@WebFluxTest(ChargingStationController.class)
class ChargingStationControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;

	@MockBean
	private ChargingStationService chargingStationService;
	
	@Test
    public void testGetAll() {
		ChargingStationData response = new ChargingStationData();
		List<Item> items = new ArrayList<>();
		Item item1 = new Item(1000L, "Airport", true);
		Item item2 = new Item(2000L, "Phoenix Mall", false);
		Item item3 = new Item(3000L, "Kharadi", true);
		items.add(item1);
		items.add(item2);
		items.add(item3);
		response.setChargingStations(items);
        Mono<ChargingStationData> chargingStationMono = Mono.just(response);

        when(chargingStationService.getAll("Pune")).thenReturn(chargingStationMono);

        webTestClient.get()
                .uri("/api/v1/station/Pune")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ChargingStationData.class)
                .value(resp -> resp.getChargingStations().size(), equalTo(3));
    }
	
	@Test
    public void testGetAll_ThrowsException() {
        when(chargingStationService.getAll("Pune")).thenThrow(new ChargingStationServiceException("Error!"));

        webTestClient.get()
                .uri("/api/v1/station/Pune")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
	
}
