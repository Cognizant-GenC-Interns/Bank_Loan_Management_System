package com.cts.blms.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.cts.blms.entity.LoanApplication;
import com.cts.blms.entity.Repayment;

public interface RepaymentService {

	public Date getPaymentSchedule(Long loanApplicationId);

	public void makePayment(Long repaymentId, double amountPaid, LocalDate paymentDate);

	public String getOutstandingBalance(LoanApplication loanApplication);

	public void createRepayment(Repayment repayment);

	public List<Repayment> getRepayementByLoanApplicationById(Long loanApplicationId);

	public Repayment getRepayment(Long repaymentId);

}
