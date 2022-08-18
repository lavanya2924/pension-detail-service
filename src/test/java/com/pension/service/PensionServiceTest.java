package com.pension.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.pension.entity.BankDetail;
import com.pension.entity.DetailsOfPensioner;
import com.pension.exception.AadhaarNotFoundException;
import com.pension.repository.BankDetailRepository;
import com.pension.repository.PensionerDetailRepository;
import com.pension.util.TypeOfBank;
import com.pension.util.TypeOfPension;

@SpringBootTest
class PensionServiceTest {

	@Autowired
	private PensionService service;

	@MockBean
	private PensionerDetailRepository detailRepository;

	@MockBean
	private BankDetailRepository bankRepository;

	DetailsOfPensioner pensioner;

	@BeforeEach
	void setup() {
		BankDetail bank = BankDetail.builder().accountNumber(67890543215l).bankType(TypeOfBank.PUBLIC).name("SBI")
				.build();
		pensioner = DetailsOfPensioner.builder().aadhaar(987654321098l).allowances(2000).dob(new Date()).name("ABC")
				.pan("BAJPC4350M").salaryEarned(15000).bankDetail(bank).typeofPension(TypeOfPension.SELF).build();
		Mockito.when(detailRepository.findByAadhaar(987654321098l)).thenReturn(pensioner);

	}

	@Test
	@DisplayName("CASE 1 :Get Pensioner Detail by using Aadhaar")
	void providePensionerDetailBasedOnAadhaarTest1() {
		long aadhaar = 987654321098l;
		DetailsOfPensioner pensioner = service.fetchDetails(aadhaar);
		assertThat(aadhaar).isEqualTo(pensioner.getAadhaar());
	}

	@Test
	@DisplayName("CASE 2 :Get Pensioner Detail by using Aadhaar")
	void providePensionerDetailBasedOnAadhaarTest2() {
		assertThatExceptionOfType(AadhaarNotFoundException.class).isThrownBy(() -> {
			long aadhaar = 987654321099l;
			service.fetchDetails(aadhaar);
		});
	}

	@Test
	@DisplayName("CASE 1 :Insert New Pensioner with fault")
	void insertPensionerTest1() {
		pensioner.getBankDetail().setName("SB");
		String res = service.insert(pensioner);
		assertThat(res).isNull();
	}

	@Test
	@DisplayName("CASE 2 :Insert New Pensioner with fault")
	void insertPensionerTest2() {
		pensioner.getBankDetail().setAccountNumber(78965);
		String res = service.insert(pensioner);
		assertThat(res).isNull();
	}

	@Test
	@DisplayName("CASE 3 :Insert New Pensioner ")
	void insertPensionerTest3() {
		pensioner.setAadhaar(987654321998l);
		pensioner.setPan("BAJPC4360M");
		Mockito.when(detailRepository.findByAadhaar(987654321998l)).thenReturn(null);
		Mockito.when(detailRepository.findByPan("BAJPC4360M")).thenReturn(null);
		String res = service.insert(pensioner);
		assertThat(res).isEqualTo("Saved");
	}

	@Test
	@DisplayName("CASE 1 :Update Pensioner with Name Change ")
	void updatePensionerTest1() {
		pensioner.setName("ABCD");
		String res = service.update(pensioner);
		assertThat(res).isNull();
	}

	@Test
	@DisplayName("CASE 2 :Update Pensioner with PAN Change ")
	void updatePensionerTest2() {
		pensioner.setPan("BAJPC4360M");
		String res = service.update(pensioner);
		assertThat(res).isNull();
	}

	@Test
	@DisplayName("CASE 3 :Update Pensioner")
	void updatePensionerTest3() {
		BankDetail bank = BankDetail.builder().accountNumber(67890543215l).bankType(TypeOfBank.PUBLIC).name("SBI")
				.build();
		Mockito.when(detailRepository.findByAadhaar(987654321098l)).thenReturn(pensioner);
		Mockito.when(bankRepository.findByAccountNumber(67890543215l)).thenReturn(bank);
		pensioner.setAllowances(8000);
		String res = service.update(pensioner);
		assertThat(res).isEqualTo("Updated");
	}
	
	@Test
	@DisplayName("CASE 1 :Map Pensioner")
	void mapPensionerTest1() {
		Map<String, Object> map = Map.of("name", "ABC", "dob", "2022-06-01", "aadhaar", "987654321098", "salaryEarned",
		15000, "allowances", 2000, "typeofPension", "SELF", "pan", "BAJPC4350N", "bankDetail",
		Map.of("name", "SBI", "accountNumber", "67890543215", "bankType", "PUBLIC"));
		DetailsOfPensioner mapPensionerDetail = service.mapPensionerDetail(map);
		assertThat(mapPensionerDetail.getAadhaar()).isEqualTo(pensioner.getAadhaar());
	}

}
