package com.cts.blms.service;



import java.time.LocalDate;
import java.util.List;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;

import jakarta.validation.Valid;

 
 
public interface LoanApplicationService {

	LoanApplication addLoanApplication(@Valid LoanApplication loanApplication);

	List<LoanApplication> getAllLoanapplicationDetails();

	List<LoanApplication> getLoanApplicationByCustomer(Customer customer);

	LoanApplication approveLoan(Long id, LocalDate approveDate);

	LoanApplication rejectLoan(Long id);

	LoanApplication getLoanApplicationById(Long id);
}

