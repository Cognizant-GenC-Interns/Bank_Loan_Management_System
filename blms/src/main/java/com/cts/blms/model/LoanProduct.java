package com.cts.blms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "loanproduct")
public class LoanProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer loanProductId;

	@Column(nullable = false, length = 100)
	private String productName;

	@Column(nullable = false)
	private Double interestRate;

	@Column(nullable = false)
	private Long minAmount;

	@Column(nullable = false)
	private Long maxAmount;

	@Column(nullable = false)
	private Integer tenure;
}
