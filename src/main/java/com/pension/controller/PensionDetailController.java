package com.pension.controller;

import java.util.Map;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pension.entity.DetailsOfPensioner;
import com.pension.entity.Message;
import com.pension.service.PensionService;

@RestController
@CrossOrigin(origins = { "http://localhost:4200","*" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.DELETE, RequestMethod.PUT })
public class PensionDetailController {

	private Logger log = LoggerFactory.getLogger(PensionDetailController.class);

	@Autowired
	private PensionService service;
	
	@GetMapping("/test")
	public String awsHealthCheck(){
		return "It is working";
	}

	@GetMapping(value = "/PensionerDetailByAadhaar/{aadhaarNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DetailsOfPensioner> fetchingPensionerDetailsByAadhar(
			@RequestHeader(value = "Authorization") String token, @PathVariable String aadhaarNumber)
			throws AuthenticationException {

		service.validateAuthorization(token);
		log.info("Authorized Request. Fetch Service is initiated ...");
		DetailsOfPensioner pensionerDetails = service.fetchDetails(Long.parseLong(aadhaarNumber.trim()));
		log.info("Fetch Service is completed ...");
		return new ResponseEntity<>(pensionerDetails, HttpStatus.OK);
	}

	@PostMapping(value = "/SavePensioner", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json")
	public ResponseEntity<Object> savePensionerDetails(@RequestBody Map<String,Object> pensionDetail,
			@RequestHeader(value = "Authorization") String token)
			throws AuthenticationException {
		service.validateAuthorization(token);
		log.info("Authorized Request. Save Request is initiated ...");
		String result = service.insert(service.mapPensionerDetail(pensionDetail));
		log.debug("Pensioner Detail = {}",service.mapPensionerDetail(pensionDetail));
		if (result != null) {
			log.info("Save Request is completed ...");
			return new ResponseEntity<>(new Message(result), HttpStatus.CREATED);
		}
		log.info("Save Request is rejected ...");
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping(value = "/UpdatePensioner", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json")
	public ResponseEntity<Object> updatePensionerDetails(@RequestBody Map<String,Object> pensionDetail,
			@RequestHeader(value = "Authorization") String token)
			throws AuthenticationException {
		service.validateAuthorization(token);
		log.info("Authorized Request. Update Request is initiated ...");
		String result = service.update(service.mapPensionerDetail(pensionDetail));
		log.debug("Pensioner Detail = {}",service.mapPensionerDetail(pensionDetail));
		if (result != null) {
			log.info("Updated successfully ...");
			return new ResponseEntity<>(new Message(result), HttpStatus.OK);
		}
		log.info("Updated Request is rejected ...");
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping(value = "/DeletePensioner/{aadhaarNumber}" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deletePensioner(@PathVariable String aadhaarNumber,
			@RequestHeader(value = "Authorization") String token)
			throws AuthenticationException {
		service.validateAuthorization(token);
		log.info("Authorized Request. Delete Request is initiated ...");
		String result = service.delete(Long.parseLong(aadhaarNumber.trim()));
		if (result != null) {
			log.info("Delete successfully ...");
			return new ResponseEntity<>(new Message(result), HttpStatus.OK);
		}
		log.info("Delete Request is rejected ...");
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
