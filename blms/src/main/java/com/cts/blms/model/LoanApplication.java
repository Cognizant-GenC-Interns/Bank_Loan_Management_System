package com.cts.blms.model;

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
	private Date applicationDate;
	
	@Column(name="name_of_asset")
	private String nameOfAsset;
	
	@Column(name = "asset_Name")
	private String assetName;

	
	@Lob
	@Column(name = "asset_Image" , columnDefinition = "LONGBLOB")
	private byte[] assetImage;
	
	@ManyToOne
	@JoinColumn(name = "customer_id",referencedColumnName ="customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "loan_product_id",referencedColumnName = "loanProductId")
	private LoanProduct loanProduct;
	
	@Column(name="affordable_amount")
	private double maxAffordableAmount;
	
	@Column(name="Emi_amount")
	private double emiAmount;
	
	@Enumerated(EnumType.STRING)
    @Column(name="loan_application_staus")
	private LoanApplicationStatus loanApplicationStatus;
}
