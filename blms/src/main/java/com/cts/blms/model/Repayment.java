package com.cts.blms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Repayment")
public class Repayment {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name = "repayment_id")
	 private int repaymentId;
	 
	 @JoinColumn(name = "application_id", referencedColumnName = "applicationId", nullable = false)
	 @Column(name="application_id")
	 private int applicationId;
	 
	 @Column(name="due_date")
	 private Date dueDate;
	 
	 @Column(name="amount_due")
	 private double amountDue;
	 
	 @Column(name="payment_date")
	 private Date paymentDate;
	 
	 @Column(name="payment_status")
	 private PaymentStatus paymentStatus;
	 
	
}
