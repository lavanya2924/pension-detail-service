package com.pension.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.pension.util.TypeOfBank;

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
@Table(name = "BANK_DETAIL", uniqueConstraints = @UniqueConstraint(columnNames = { "BANK_USER_ID", "USER_ACCOUNT_NUMBER" }))
public class BankDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BANK_USER_ID")
	private long id;

	@Column(name = "BANK_NAME")
	private String name;

	@Column(name = "USER_ACCOUNT_NUMBER")
	private long accountNumber;

	@Column(name = "BANK_TYPE")
	private TypeOfBank bankType;
	
}
