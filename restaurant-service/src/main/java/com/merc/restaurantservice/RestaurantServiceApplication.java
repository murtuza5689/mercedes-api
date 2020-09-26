package com.merc.restaurantservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RestaurantServiceApplication {

	@Value("${here.maps.base.url}")
	private String baseURL;
	
	public static void main(String[] args) {
		SpringApplication.run(RestaurantServiceApplication.class, args);
	}
	
	@Bean
	public WebClient defaultWebClient() {
		TcpClient tcpClient = TcpClient
						.create()
						.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
						.doOnConnected(connection -> connection
								.addHandlerLast(new ReadTimeoutHandler(2))
								.addHandlerLast(new WriteTimeoutHandler(2)));

		return WebClient.builder().baseUrl(baseURL)
				.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

}
