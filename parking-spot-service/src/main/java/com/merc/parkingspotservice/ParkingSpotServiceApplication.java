package com.merc.parkingspotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEncryptableProperties
public class ParkingSpotServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingSpotServiceApplication.class, args);
	}

}
