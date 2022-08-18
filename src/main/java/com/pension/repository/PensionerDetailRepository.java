package com.pension.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pension.entity.DetailsOfPensioner;

@Repository
@Transactional
public interface PensionerDetailRepository extends JpaRepository<DetailsOfPensioner, Long> {
	public DetailsOfPensioner findByAadhaar(long aadhaar);

	public int deleteByAadhaar(long aadhaar);

	public DetailsOfPensioner findByPan(String pan);

	@Modifying
	@Query(nativeQuery = true, value = "UPDATE PENSIONER_DETAIL SET "
			+ "SALARY =?1, ALLOWANCES =?2, PENSION_CLASSIFICATION =?3, BANK_DETAIL_BANK_USER_ID =?4 "
			+ "WHERE AADHAAR =?5")
	public void updatePensionerDetail(double salaryEarned, int allowances,
			String typeofPension, long bankUserId, long aadhaar);

}
