package com.cts.blms.service;

import java.util.ArrayList;
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
	public LoanProduct updateLoanProduct(LoanProduct loan) {
		// TODO Auto-generated method stub
		LoanProduct existingProduct=loanProductRepository.findById(Long.valueOf(loan.getLoanProductId())).orElseThrow(() -> new RuntimeException("Loan Product not found with ID: " + loan.getLoanProductId()));
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

	@Override
	public List<String> getName() {
		// TODO Auto-generated method stub
		List<LoanProduct> products = loanProductRepository.findAll();
		List<String> loanProductName = new ArrayList<>();
		for(LoanProduct productObj:products)
		{
			loanProductName.add(productObj.getProductName());
		}
		
		return loanProductName;
		
	}

	@Override
	public void deleteLoanProduct(Long loanProductId) {
		// TODO Auto-generated method stub
		LoanProduct existingProduct=loanProductRepository.findById(Long.valueOf(loanProductId)).orElseThrow(() -> new RuntimeException("Loan Product not found with ID: " + loanProductId));
		loanProductRepository.delete(existingProduct);
	}
}
