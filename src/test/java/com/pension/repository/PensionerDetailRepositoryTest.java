package com.pension.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import com.pension.entity.BankDetail;
import com.pension.entity.DetailsOfPensioner;
import com.pension.util.TypeOfBank;


@DataJpaTest
class PensionerDetailRepositoryTest {

	@Autowired
	private PensionerDetailRepository pensionerDetailRepository;

	@Autowired
	private TestEntityManager em;

	@BeforeEach
	void setUp() {
		BankDetail bankDetail = BankDetail.builder().accountNumber(89076543245l).bankType(TypeOfBank.PUBLIC).name("IOB")
				.build();
		
		DetailsOfPensioner pensioner = DetailsOfPensioner.builder().aadhaar(987654321098l)
		.allowances(2000)
		.dob(new Date())
		.name("ABC")
		.pan("BAJPC4350N")
		.salaryEarned(15000)
		.bankDetail(bankDetail)
		.build();
		
		em.persist(pensioner);
	}
		
	@Test
	@DisplayName("Get Pensioner by using Aadhaar Number")
	void findByAadhaarTest() {
		DetailsOfPensioner pensioner = pensionerDetailRepository.findByAadhaar(987654321098l);
		assertThat(pensioner.getPan()).isEqualTo("BAJPC4350N");
	}
	
	@Test
	@DisplayName("Get Pensioner by using PAN")
	void findByPANTest() {
		DetailsOfPensioner pensioner = pensionerDetailRepository.findByPan("BAJPC4350N");
		assertThat(pensioner.getName()).isEqualTo("ABC");
	}
	

}
