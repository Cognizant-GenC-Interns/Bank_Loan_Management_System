package com.cts.blms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.LoanProductService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/bigbank")
public class LoanProductController {
	
	@Autowired
	LoanProductService loanProductService;
	
	@PostMapping("/loanproducts")
    public LoanProduct addLoanProduct(@Valid @ModelAttribute("newLoanProduct") LoanProduct loanProduct) {
        return loanProductService.addLoanProduct(loanProduct);
    }
	
	
	@PostMapping("/loanproducts/{loanproductid}")
    public LoanProduct updateLoanProduct(@Valid @ModelAttribute("existingLoanProduct") LoanProduct loanProduct) {
        return loanProductService.updateLoanProduct(loanProduct);
    }
	
	@PostMapping("/loanproducts/{loanproductid}")
	public void deleteLoanProduct(@PathVariable Long loanProductId) {
	    loanProductService.deleteLoanProduct(loanProductId);
	}

	
	
	@GetMapping("/loanproducts")
    public List<LoanProduct> getLoanProductsDetails() {
        return loanProductService.getLoanProductDetails();
    }
	
}
