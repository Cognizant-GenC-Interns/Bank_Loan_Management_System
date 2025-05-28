package com.cts.blms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Customer;
import com.cts.blms.model.KycStatus;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanApplicationStatus;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.repository.LoanApplicationRepository;

import jakarta.validation.Valid;

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

	
}
