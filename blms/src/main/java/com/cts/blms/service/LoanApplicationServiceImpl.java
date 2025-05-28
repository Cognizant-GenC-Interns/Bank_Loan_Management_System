package com.cts.blms.service;

import java.util.List;

 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanApplicationStatus;
import com.cts.blms.model.LoanProduct;
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
	public void deleteLoan(long id) {
		LoanApplication loanApplication=repository.findById(id).orElse(null);
		repository.delete(loanApplication);
	}

	@Override
	public LoanApplication getEligibility(Customer customer, LoanProduct loanProduct,LoanApplication loanApplication) {
		// TODO Auto-generated method stub
		double monthlySalary = customer.getAnnualSalary() / 12.0;
        double affordableAmount = monthlySalary * 0.4;
        double principle=loanApplication.getRequestAmount();
        double monthlyIntrestRate=loanProduct.getInterestRate()/12/100;
        double ratePower=Math.pow(1+monthlyIntrestRate,loanProduct.getTenure());
        double emiAmount=(principle*monthlyIntrestRate*ratePower)/(ratePower-1);
        
        loanApplication.setEmiAmount(emiAmount);
        loanApplication.setCustomer(customer);
        loanApplication.setLoanProduct(loanProduct);
        
        int creditScore=customer.getCreditScore();
        
        if(creditScore<500&&emiAmount<affordableAmount) {
        	String s="credit score is bad and cannot affordable";
        	loanApplication.setEligibility(s);
        }
        else if(creditScore<500&&emiAmount>=affordableAmount) {
        	String s="credit score is bad but can affordable";
        	loanApplication.setEligibility(s);
        }
        else if(creditScore>=500&&emiAmount<affordableAmount) {
        	String s="credit score is good but cannot affordable";
        	loanApplication.setEligibility(s);
        }
        else if(creditScore>=500&&emiAmount>=affordableAmount){
        	String s="credit score is good and affordable";
        	loanApplication.setEligibility(s);
        }
		
		return loanApplication;
		
	}

	
}