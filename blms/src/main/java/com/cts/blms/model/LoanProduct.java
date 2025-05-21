//package com.cts.blms.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "loanproduct")
//public class LoanProduct {
//	
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer loanProductId;
//	
//	@Column(nullable = false, length = 100)
//    private String ProductName;
//	
//	@Column(nullable = false)
//    private Double interestRate;
//
//    @Column(nullable = false)
//    private Double minAmount;
//
//    @Column(nullable = false)
//    private Double maxAmount;
//
//    @Column(nullable = false)
//    private Integer tenure;
//}
