package com.pension.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pension.entity.BankDetail;
@Transactional
@Repository
public interface BankDetailRepository extends JpaRepository<BankDetail, Long> {
	public BankDetail findByAccountNumber(long accountNumber);
	
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO BANK_DETAIL (BANK_NAME,USER_ACCOUNT_NUMBER,BANK_TYPE) VALUES (?1,?2,?3) ")
	public  void saveBankDetail(String name, long accountNumber, int typeOfPension);
}
