package com.productheaven.user.service.exception;

public class InvalidRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRequestException() {
		
	}
	
	public InvalidRequestException (String message) {
		super(message);
	}
}
