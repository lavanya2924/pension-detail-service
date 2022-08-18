package com.pension.controller;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.pension.entity.BankDetail;
import com.pension.entity.DetailsOfPensioner;
import com.pension.exception.AadhaarNotFoundException;
import com.pension.service.PensionService;
import com.pension.util.TypeOfBank;
import com.pension.util.TypeOfPension;

@WebMvcTest(value = { PensionDetailController.class })
class PensionDetailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PensionService service;

	private DetailsOfPensioner pensioner;

	String auth;

	@BeforeEach
	void setUp() {

		auth = "";

		BankDetail bankDetail = BankDetail.builder().accountNumber(89076543245l).bankType(TypeOfBank.PUBLIC).name("IOB")
				.build();

		pensioner = DetailsOfPensioner.builder().aadhaar(987654321098l).allowances(2000).dob(Date.valueOf("2022-12-12"))
				.name("ABC").pan("BAJPC4350N").salaryEarned(15000).bankDetail(bankDetail)
				.typeofPension(TypeOfPension.FAMILY).build();
	}

	@Test
	void getPensionerByAadhaarTest1() throws Exception {
		Mockito.when(service.fetchDetails(987654321098l)).thenThrow(new AadhaarNotFoundException(""));
		mockMvc.perform(
				MockMvcRequestBuilders.get("/PensionerDetailByAadhaar/987654321098").header("Authorization", auth))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void getPensionerByAadhaarTest2() throws Exception {
		Mockito.when(service.fetchDetails(987654321098l)).thenReturn(pensioner);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/PensionerDetailByAadhaar/987654321098").header("Authorization", auth))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void deletePensionerByAadhaarTest1() throws Exception {
		Mockito.when(service.delete(987654321098l)).thenReturn("Deleted");
		mockMvc.perform(MockMvcRequestBuilders.delete("/DeletePensioner/987654321098").header("Authorization", auth))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void deletePensionerByAadhaarTest2() throws Exception {
		Mockito.when(service.delete(987654321098l)).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.delete("/DeletePensioner/987654321098").header("Authorization", auth))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	@DisplayName("Save Pensioner :  Already Exist Aadhaar")
	void savePensionerTest1() throws Exception {
		Mockito.when(service.insert(pensioner)).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/SavePensioner").header("Authorization", auth)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\r\n" + "    \"name\": \"ABC\",\r\n" + "    \"dob\": \"1997-02-28\",\r\n"
						+ "    \"aadhaar\": 987654321098,\r\n" + "    \"salaryEarned\": 15000.0,\r\n"
						+ "    \"allowances\": 2000,\r\n" + "    \"typeofPension\": \"FAMILY\",\r\n"
						+ "    \"bankDetail\": {\r\n" + "        \"name\": \"IOB\",\r\n"
						+ "        \"accountNumber\": 89076543245,\r\n" + "        \"bankType\": \"PUBLIC\"\r\n"
						+ "    },\r\n" + "    \"pan\": \"BAJPC4350N\"\r\n" + "}"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	@DisplayName("Update Test -  New Aadhaar")
	void updatePensionerTest2() throws Exception {
		Mockito.when(service.update(pensioner)).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/UpdatePensioner").header("Authorization", auth)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"name\": \"ABC\",  \"dob\": \"\"," + " \"aadhaar\": 987654321099, \"salaryEarned\": 15000,"
						+ " \"allowances\": 2000, \"typeofPension\": \"FAMILY\","
						+ " \"bankDetail\": { \"name\": \"IOB\","
						+ "  \"accountNumber\": 89076543245, \"bankType\": \"PRIVATE\" "
						+ "},\"pan\": \"BAJPC4350N\" }"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

}
