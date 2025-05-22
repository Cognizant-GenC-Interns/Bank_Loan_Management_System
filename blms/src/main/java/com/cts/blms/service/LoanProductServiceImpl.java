package com.cts.blms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.LoanProduct;
import com.cts.blms.repository.LoanProductRepository;

@Service
public class LoanProductServiceImpl implements LoanProductService{
	@Autowired
	private LoanProductRepository loanProductRepository;
	
	@Override
	public LoanProduct addLoanProduct(LoanProduct loan) {
		// TODO Auto-generated method stub
		return loanProductRepository.save(loan);
	}

	@Override
	public LoanProduct updateLoanProduct(Integer loanProductId,LoanProduct loan) {
		// TODO Auto-generated method stub
		LoanProduct existingProduct=loanProductRepository.findById(Long.valueOf(loanProductId)).orElseThrow(() -> new RuntimeException("Loan Product not found with ID: " + loanProductId));
		if (loan.getProductName() != null) existingProduct.setProductName(loan.getProductName());
	    if (loan.getInterestRate() != null) existingProduct.setInterestRate(loan.getInterestRate());
	    if (loan.getMinAmount() != null) existingProduct.setMinAmount(loan.getMinAmount());
	    if (loan.getMaxAmount() != null) existingProduct.setMaxAmount(loan.getMaxAmount());
	    if (loan.getTenure() != null) existingProduct.setTenure(loan.getTenure());
	    return loanProductRepository.save(existingProduct);
	}

	@Override
	public List<LoanProduct> getLoanProductDetails() {
	    List<LoanProduct> products = loanProductRepository.findAll();
	    
	    if (products.isEmpty()) {
	        throw new RuntimeException("No Loan Product exists");
	    }

	    return products;
	}
}
