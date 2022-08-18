package com.pension.exception;

import javax.naming.AuthenticationException;

public class JwtTokenExpiredException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public JwtTokenExpiredException(String message) {
		super(message);
	}

}
