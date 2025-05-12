package com.management_system.customer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class CustomerEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int customerId;
	
	@Column(name = "customer_name",nullable=false)
	private String customerName;
	
	@Column(name = "email",nullable=false,unique=true)
	private String email;
	
	@Column(name = "phone",nullable=false)
	private String phone;
	
	@Column(name = "address")
	private String address;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "kyc_status")
	private KycStatus kycStatus;
}