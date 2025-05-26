package com.cts.blms.service;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.repository.LoanApplicationRepository;
 
import jakarta.validation.Valid;
 
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {
	@Autowired
	LoanApplicationRepository repository;

 
	public LoanApplication addLoanApplication(LoanApplication loanApplication) {
	    Customer customer = loanApplication.getCustomerId();
	    LoanProduct loanProduct = loanApplication.getLoanProductId();
 
	    if (customer != null && loanProduct != null) {
	        int annualSalary = customer.getAnnualSalary();
	        int tenureInMonths = loanProduct.getTenure();
	        double annualInterestRate=loanProduct.getInterestRate();
	        double monthlySalary = annualSalary / 12.0;
	        double maxAffordableAmount=monthlySalary*0.4;
	        double principal = loanApplication.getRequestAmount();
	        double monthlyRate = annualInterestRate / 12 / 100;
	        double ratePower = Math.pow(1 + monthlyRate, tenureInMonths);
	        double emi = (principal * monthlyRate * ratePower) / (ratePower - 1);
	        loanApplication.setEmiAmount(emi);
 
	    }
 
	    return repository.save(loanApplication);
	}
 
 
	@Override
	public List<LoanApplication> getAllApplications() {
		return repository.findAll();
	}
}