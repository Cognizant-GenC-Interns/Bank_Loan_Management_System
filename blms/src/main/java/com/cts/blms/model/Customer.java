package com.cts.blms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	
	@Column(name = "profile_image_name")
	private String profileImageName;
	
	@Lob
	@Column(name = "profile_image" , columnDefinition = "LONGBLOB")
	private byte[] profileImage;
	
	
	@NotBlank(message = "Email is mandatory")
	@Column(name="email")
	private String email;
	
	@NotBlank(message = "password is mandatory")
	@Column(name="password")
	private String password;
	
	@NotBlank(message = "phone is mandatory")
	@Column(name="phone")
	private String phone;
	
	@NotBlank(message = "address is mandatory")
	@Column(name="address")
	private String address;
	
	@Enumerated(EnumType.STRING)
    @Column(name="kyc_status")
	private KycStatus kycStatus;
	
	@Column(name = "annual_salary")
	private Integer annualSalary;
	
	
	@Column(name = "pan_image_name")
	private String panImageName;
	
	@Lob
	@Column(name = "pan_card_image" , columnDefinition = "LONGBLOB")
	private byte[] panImage;
	
	@Column(name = "salary_slip_image_name")
	private String salarySlipImageName;
	
	@Lob
	@Column(name = "salary_slip_image" , columnDefinition = "LONGBLOB")
	private byte[] salarySlipImage;

	@Column(name = "credit_score")
	private Integer creditScore;

	
}
