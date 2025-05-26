package com.cts.blms.service;
 
 
import java.util.List;
 
import com.cts.blms.model.LoanApplication;
 
import jakarta.validation.Valid;
 
 
public interface LoanApplicationService {
 
	LoanApplication addLoanApplication(@Valid LoanApplication loanApplication);
	List<LoanApplication> getAllApplications();
}