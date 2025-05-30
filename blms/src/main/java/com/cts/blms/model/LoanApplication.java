package com.cts.blms.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "loanapplication")
public class LoanApplication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long loanApplicationId;
	
	@Column(name = "request_Amount")
	private double requestAmount;
	
	@Column(name="application_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate applicationDate;
	
	
	@Column(name="approved_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate approvedDate;
	
	@Column(name="name_of_asset")
	private String nameOfAsset;
	
	@Column(name = "asset_Name")
	private String assetName;
	
	@Column(name="eligibility")
	private String eligibility;
	
	@Lob
	@Column(name = "asset_Image" , columnDefinition = "LONGBLOB")
	private byte[] assetImage;
	
	@ManyToOne
	@JoinColumn(name = "customer_id",referencedColumnName ="customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "loan_product_id",referencedColumnName = "loanProductId")
	private LoanProduct loanProduct;
	
	@Column(name="Emi_amount")
	private double emiAmount;
	
	@Column(name="Balance")
	private double balance;
	
	@Enumerated(EnumType.STRING)
    @Column(name="loan_application_staus")
	private LoanApplicationStatus loanApplicationStatus;
}