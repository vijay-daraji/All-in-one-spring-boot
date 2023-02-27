package com.vijay.springbootdemo.util;

public class ErrorResponse {
	
	private String error_message;
	private String details;
	
	public ErrorResponse(String error_message, String details) {
		super();
		this.error_message = error_message;
		this.details = details;
	}
	
	public String getError_message() {
		return error_message;
	}
	public String getDetails() {
		return details;
	}
	 
	  

}
