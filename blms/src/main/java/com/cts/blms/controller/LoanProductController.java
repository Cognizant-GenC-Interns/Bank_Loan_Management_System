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
    public String addLoanProduct(@Valid @ModelAttribute("newLoanProduct") LoanProduct loanProduct) {
        loanProductService.addLoanProduct(loanProduct);
        return "redirect:/adminDashboard";
    }
	
	
	@PostMapping("/loanproducts/{loanproductid}")
    public String updateLoanProduct(@Valid @ModelAttribute("existingLoanProduct") LoanProduct loanProduct) {
        loanProductService.updateLoanProduct(loanProduct);
        return "redirect:/adminDashboard";
    }
	
	@PostMapping("/loanproducts/{loanproductid}")
	public String deleteLoanProduct(@PathVariable Long loanProductId) {
	    loanProductService.deleteLoanProduct(loanProductId);
	    return "redirect:/adminDashboard";
	}

	
	
	@GetMapping("/loanproducts")
    public List<LoanProduct> getLoanProductsDetails() {
        return loanProductService.getLoanProductDetails();
    }
	
}
