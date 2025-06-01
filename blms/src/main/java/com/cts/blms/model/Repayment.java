package com.cts.blms.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Repayment")
public class Repayment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "repayment_id")
	private Integer repaymentId;

	@ManyToOne
	@JoinColumn(name = "application_id", referencedColumnName = "loanApplicationId", nullable = false)
	private LoanApplication loanApplication;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "payment_status")
	private PaymentStatus paymentStatus;

	@Column(name = "amount_due")
	private Double amountDue;

	@Column(name = "interest_amount")
	private Double interestAmount;

	@Column(name = "fine_amount")
	private Double fineAmount;

	@Column(name = "principal_paid")
	private Double principalPaid;

	@Column(name = "interest_paid")
	private Double interestPaid;

}
