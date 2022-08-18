package com.pension.exception;

public class AadhaarNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AadhaarNotFoundException(String message) {
		super(message);
	}
}
