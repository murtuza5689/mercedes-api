package com.merc.chargingstationservice.exception;

public class ChargingStationServiceException extends RuntimeException {

	public ChargingStationServiceException(String msg) {
		super(msg);
	}
	
	public ChargingStationServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
