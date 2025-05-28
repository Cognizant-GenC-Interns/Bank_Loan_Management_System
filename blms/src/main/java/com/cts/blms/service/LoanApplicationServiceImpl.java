package com.cts.blms.service;

import java.time.LocalDate;
import java.util.List;

 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanApplicationStatus;
import com.cts.blms.repository.LoanApplicationRepository;



 
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {
	
	@Autowired
	LoanApplicationRepository repository;

	public LoanApplication addLoanApplication(LoanApplication loanApplication) {
		loanApplication.setLoanApplicationStatus(LoanApplicationStatus.PENDING);
	    return repository.save(loanApplication);
	}

	@Override
	public List<LoanApplication> getAllLoanapplicationDetails() {
		return repository.findAll();
	}

	@Override
	public List<LoanApplication> getLoanApplicationByCustomer(Customer customer) {
		return repository.findByCustomer(customer);
	}

	

	@Override
	public LoanApplication rejectLoan(Long id) {
		// TODO Auto-generated method stub
		LoanApplication loanApplication=repository.findById(id).get();
		loanApplication.setLoanApplicationStatus(LoanApplicationStatus.REJECTED);
		return repository.save(loanApplication);		
	}

	@Override
	public LoanApplication getLoanApplicationById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

	@Override
	public LoanApplication approveLoan(Long id, LocalDate approveDate) {
		// TODO Auto-generated method stub
		LoanApplication loanApplication=repository.findById(id).get();
		loanApplication.setLoanApplicationStatus(LoanApplicationStatus.APPROVED);
		loanApplication.setApprovedDate(approveDate);
		return repository.save(loanApplication);
	}

	
}