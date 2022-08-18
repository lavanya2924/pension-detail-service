package com.pension.service;

import java.sql.Date;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pension.entity.BankDetail;
import com.pension.entity.DetailsOfPensioner;
import com.pension.exception.AadhaarNotFoundException;
import com.pension.repository.BankDetailRepository;
import com.pension.repository.PensionerDetailRepository;
import com.pension.util.JwtToken;
import com.pension.util.TypeOfBank;
import com.pension.util.TypeOfPension;

@Service
public class PensionService {

	private Logger log = LoggerFactory.getLogger(PensionService.class);

	@Autowired
	private JwtToken jwt;

	@Autowired
	private PensionerDetailRepository detailRepository;

	@Autowired
	private BankDetailRepository bankRepository;

	public void validateAuthorization(String token) throws AuthenticationException {
		jwt.validateToken(token);
	}

	public String delete(long aadhaar) {
		return detailRepository.deleteByAadhaar(aadhaar) != 0 ? "Deleted" : null;
	}

	public DetailsOfPensioner fetchDetails(long aadhaar) {
		log.debug("Fetching Pensioner Details....");
		DetailsOfPensioner pensioner = detailRepository.findByAadhaar(aadhaar);
		if (pensioner == null) {
			throw new AadhaarNotFoundException("Aadhaar Number " + aadhaar + " is not found in Database.");
		}
		log.debug("Fetched Pensioner Details....");
		return pensioner;
	}

	public String insert(DetailsOfPensioner pensionDetail) {
		log.debug("Inserting Pensioner Details....");
		BankDetail bank = pensionDetail.getBankDetail();
		if (bank.getName().length() > 2 && String.valueOf(bank.getAccountNumber()).length() > 5
				&& bankRepository.findByAccountNumber(bank.getAccountNumber()) == null) {
			if (detailRepository.findByAadhaar(pensionDetail.getAadhaar()) == null
					&& detailRepository.findByPan(pensionDetail.getPan()) == null) {
				DetailsOfPensioner pensioner = detailRepository.save(pensionDetail);
				log.debug("Inserted Pensioner Details....\nPensioner : {}", pensioner);
			} else {
				log.debug("Not Inserted : Already Exist Aadhaar or PAN");
				return null;
			}
		} else {
			log.debug("Not Inserted : Already Exist Bank Detail or Not Valid Bank Bank Details....");
			return null;
		}
		return "Saved";
	}

	public String update(DetailsOfPensioner pensionDetail) {
		log.debug("Updating Pensioner Details....");
		DetailsOfPensioner pensioner = detailRepository.findByAadhaar(pensionDetail.getAadhaar());
		if (pensioner != null) {
			if (!(pensionDetail.getName().equalsIgnoreCase(pensioner.getName())
					&& pensionDetail.getPan().equalsIgnoreCase(pensioner.getPan())
					&& pensionDetail.getDob().compareTo(pensioner.getDob()) == 0)) {

				log.debug("Name or DOB or PAN should not be change");
				return null;
			}

			else {
				BankDetail bank = pensionDetail.getBankDetail();
				BankDetail dbBank = bankRepository.findByAccountNumber(bank.getAccountNumber());

				if (dbBank != null && dbBank.getName().equalsIgnoreCase(bank.getName())) {
					dbBank.setBankType(bank.getBankType());
					bankRepository.save(dbBank);
					detailRepository.updatePensionerDetail(pensionDetail.getSalaryEarned(),
							pensionDetail.getAllowances(), pensionDetail.getTypeofPension().toString(), dbBank.getId(),
							pensionDetail.getAadhaar());
					log.debug("Updated Pensioner Details....");
					return "Updated";
				}
				log.debug("Not Updated : Pensioner Detail Bank Account is new ....");
			}
		}
		log.debug("Not Updated : Not Exist Pensioner Detail....");
		return null;

	}

	public DetailsOfPensioner mapPensionerDetail(Map<String, Object> pensionDetail) {
		@SuppressWarnings("unchecked")
		Map<String, Object> bank = (Map<String, Object>) pensionDetail.get("bankDetail");
		BankDetail bankDetail = BankDetail.builder().accountNumber(Long.parseLong(bank.get("accountNumber").toString()))
				.bankType(TypeOfBank.valueOf(bank.get("bankType").toString())).name(bank.get("name").toString())
				.build();

		return DetailsOfPensioner.builder().name(pensionDetail.get("name").toString())
				.aadhaar(Long.parseLong(pensionDetail.get("aadhaar").toString()))
				.dob(Date.valueOf(pensionDetail.get("dob").toString())).pan(pensionDetail.get("pan").toString())
				.salaryEarned(Double.parseDouble(pensionDetail.get("salaryEarned").toString()))
				.allowances(Integer.parseInt(pensionDetail.get("allowances").toString())).bankDetail(bankDetail)
				.typeofPension(TypeOfPension.valueOf(pensionDetail.get("typeofPension").toString())).build();
	}

}
