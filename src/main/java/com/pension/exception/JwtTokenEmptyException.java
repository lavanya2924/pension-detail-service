package com.pension.exception;

import javax.naming.AuthenticationException;

public class JwtTokenEmptyException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public JwtTokenEmptyException(String message) {
		super(message);
	}

}
