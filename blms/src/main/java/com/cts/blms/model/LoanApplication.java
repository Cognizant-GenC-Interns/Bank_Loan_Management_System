package com.cts.blms.model;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	private int loanApplicationId;
	@Column(name = "request_Amount")
	private double requestAmount;
	@Column(name = "asset_Name")
	private String assetName;
	@Lob
	@Column(name = "asset_Image" , columnDefinition = "LONGBLOB")
	private byte[] assetImage;
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customerId;
 
	@ManyToOne
	@JoinColumn(name = "loan_product_id")
	private LoanProduct loanProductId;
	@Column(name="affordable_amount")
	private double maxAffordableAmount;
	@Column(name="Emi_amount")
	private double emiAmount;
}