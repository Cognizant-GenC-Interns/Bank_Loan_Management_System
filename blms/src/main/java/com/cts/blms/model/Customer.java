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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	@Email(message = "Invalid email format")
	@Column(name="email")
	private String email;
	
	@NotBlank(message = "password is mandatory")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	@Column(name="password")
	private String password;
	
	@NotBlank(message = "phone is mandatory")
	@Pattern(regexp = "^[0-9]{10}$",
			message = "Phone number must be exactly 10 digits")
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
