package com.pension.exception;

import java.util.Date;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pension.entity.ExceptionModel;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AadhaarNotFoundException.class)
	protected ResponseEntity<ExceptionModel> handleAadhaarNotFoundException(AadhaarNotFoundException anfe) {
		String date = new Date().toString();
		String message = anfe.getMessage();
		ExceptionModel exp = new ExceptionModel(message, "Register the Detail in Portal", date, true);
		return ResponseEntity.badRequest().body(exp);
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	protected ResponseEntity<ExceptionModel> handleMissingRequestHeaderException(MissingRequestHeaderException mrhe) {
		String date = new Date().toString();
		ExceptionModel exp = new ExceptionModel("Authorization Required", "Add Authorization in the header", date,
				true);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exp);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<ExceptionModel> handleJwtTokenExpiredException(AuthenticationException ae) {
		String date = new Date().toString();
		ExceptionModel exp = new ExceptionModel(ae.getMessage(), "Provide Token", date, true);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exp);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ExceptionModel> handleAllException(Exception e) {
		String date = new Date().toString();
		String message = e.toString() + "\n" + e.getMessage();
		ExceptionModel exp = new ExceptionModel(message, e.getLocalizedMessage(), date, true);
		return ResponseEntity.badRequest().body(exp);
	}

}
