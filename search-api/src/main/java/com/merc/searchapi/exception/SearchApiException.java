package com.merc.searchapi.exception;

public class SearchApiException extends RuntimeException {

	public SearchApiException(String msg) {
		super(msg);
	}
	
	public SearchApiException(String msg, Throwable throwable) {
		super(msg, throwable);
	} 
}
