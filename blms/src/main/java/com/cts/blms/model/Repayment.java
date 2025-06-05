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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
	@NotNull(message = "Loan application must not be null")
	private LoanApplication loanApplication;

	@Column(name = "due_date")
	@NotNull(message = "Due date is required")
	private LocalDate dueDate;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "payment_status")
	private PaymentStatus paymentStatus;

	@Column(name = "amount_due")
	@PositiveOrZero(message = "Amount due must be zero or positive")
	private Double amountDue;

	@Column(name = "interest_amount")
	@PositiveOrZero(message = "Interest amount must be zero or positive")
	private Double interestAmount;

	@Column(name = "fine_amount")
	@PositiveOrZero(message = "Fine amount must be zero or positive")
	private Double fineAmount;

	@Column(name = "principal_paid")
	@PositiveOrZero(message = "Principal paid must be zero or positive")
	private Double principalPaid;

	@Column(name = "interest_paid")
	@PositiveOrZero(message = "Interest paid must be zero or positive")
	private Double interestPaid;

}
