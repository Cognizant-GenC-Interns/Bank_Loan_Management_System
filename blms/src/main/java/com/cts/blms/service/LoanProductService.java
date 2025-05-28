
package com.cts.blms.service;

import java.util.List;

import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanProduct;

public interface LoanProductService {
	public LoanProduct addLoanProduct(LoanProduct loan);
	public LoanProduct updateLoanProduct(LoanProduct loan);
	public void deleteLoanProduct(Long loanProductId); //extra feature not in description
	public List<LoanProduct> getLoanProductDetails();
	public List<String> getName();
	public LoanProduct getLoanProductById(long id);
}