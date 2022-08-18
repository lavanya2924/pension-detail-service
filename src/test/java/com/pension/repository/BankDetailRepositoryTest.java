package com.pension.repository;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.pension.entity.BankDetail;
import com.pension.util.TypeOfBank;


@DataJpaTest
class BankDetailRepositoryTest {

	@Autowired
	private BankDetailRepository bankDetailRepository;

	@Autowired
	private TestEntityManager em;

	@BeforeEach
	void setUp() {
		BankDetail bankDetail = BankDetail.builder().accountNumber(89076543245l).bankType(TypeOfBank.PUBLIC).name("IOB")
				.build();

		em.persist(bankDetail);
		bankDetailRepository.saveBankDetail("CUB", 768906789012l, 0);
	}

	@Test
	@DisplayName("Get data from BankDetail by using AccountNumber")
	void findByAccountNumberTest() {
		BankDetail bankDetail = bankDetailRepository.findByAccountNumber(89076543245l);
		assertThat(bankDetail.getName()).isEqualTo("IOB");
	}
	
	@Test
	@DisplayName("Insert Bank Detail into Data Base ")
	void saveBankDetailTest() {	
		BankDetail bankDetail = bankDetailRepository.findByAccountNumber(768906789012l);
		assertThat(bankDetail.getName()).isEqualTo("CUB");
	}

}
