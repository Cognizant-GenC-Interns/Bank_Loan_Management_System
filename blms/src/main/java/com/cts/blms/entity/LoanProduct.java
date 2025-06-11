package com.cts.blms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "loanproduct")
public class LoanProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer loanProductId;



	@Column(nullable = false, length = 100)
	@NotBlank(message = "Product name is required")
	@Size(max = 100, message = "Product name must not exceed 100 characters")
	private String productName;

	@Column(nullable = false)
	@NotNull(message = "Interest rate is required")
	private Double interestRate;

	@Column(nullable = false)
	@NotNull(message = "Minimum amount is required")
	@Min(value = 1, message = "Minimum amount must be at least 1")
	private Long minAmount;

	@Column(nullable = false)
	@NotNull(message = "Maximum amount is required")
	@Min(value = 1, message = "Maximum amount must be at least 1")
	private Long maxAmount;
	
	@Column(nullable = false)
	@NotNull(message = "Tenure is required")
	@Min(value = 1, message = "Tenure must be at least 1 month")
	private Integer tenure;
}
