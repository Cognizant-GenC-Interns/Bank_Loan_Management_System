package com.cts.blms.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.blms.entity.Customer;
import com.cts.blms.entity.LoanApplication;
import com.cts.blms.entity.LoanProduct;

import jakarta.validation.Valid;

public interface LoanApplicationService {

	LoanApplication addLoanApplication(@Valid LoanApplication loanApplication);

	List<LoanApplication> getAllLoanapplicationDetails();

	List<LoanApplication> getLoanApplicationByCustomer(Customer customer);

	void deleteLoan(long id);

	LoanApplication getEligibility(Customer customer, LoanProduct loanProduct, LoanApplication loanApplication);

	LoanApplication approveLoan(Long id, LocalDate approveDate);

	LoanApplication rejectLoan(Long id);

	LoanApplication getLoanApplicationById(Long id);

	List<LoanApplication> getAllOutstandingLoans();

	List<LoanApplication> getCustomerOutstandingLoans(Customer customer);
}
