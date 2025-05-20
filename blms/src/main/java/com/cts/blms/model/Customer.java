package com.cts.blms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	private long customerId;
	
	@NotBlank(message = "Name is mandatory")
	@Column(name="name")
	private String name;
	
	@NotBlank(message = "Email is mandatory")
	@Column(name="email")
	private String email;
	
	@NotBlank(message = "password is mandatory")
	@Column(name="password")
	private String password;
	
	@NotBlank(message = "address is mandatory")
	@Column(name="address")
	private String address;
	
	@NotBlank(message = "Kycstatus is mandatory")
	@Column(name="kyc_status")
	private KycStatus kycStatus=KycStatus.PENDING;
}
