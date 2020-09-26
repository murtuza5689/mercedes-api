package com.merc.restaurantservice.exception;

public class RestaurantServiceException extends RuntimeException {

	public RestaurantServiceException(String msg) {
		super(msg);
	}
	
	public RestaurantServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
