package com.pension.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.pension.util.TypeOfPension;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PENSIONER_DETAIL", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID", "PAN", "AADHAAR" }))
public class DetailsOfPensioner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private long id;

	@Column(name = "NAME")
	private String name; // <Pensioner name>

	@Column(name = "DATE_OF_BIRTH")
	@Temporal(TemporalType.DATE) // https://www.baeldung.com/jpa-entities
	private Date dob; // <Pensioner date of birth>

	@Column(name = "AADHAAR")
	private long aadhaar; // <Pensioner date of birth>

	@Column(name = "PAN")
	private String pan; // <Permanent account number>

	@Column(name = "SALARY")
	private double salaryEarned; // <Last earned salary by the pensioner>

	@Column(name = "ALLOWANCES")
	private int allowances; // <Sum of all the allowances>

	@Column(name = "PENSION_CLASSIFICATION")
	@Enumerated(EnumType.STRING)
	private TypeOfPension typeofPension; // <Is the pension classification self or family pension>

	@OneToOne(cascade = CascadeType.ALL)
	private BankDetail bankDetail;

}
