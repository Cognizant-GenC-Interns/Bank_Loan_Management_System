package com.cts.blms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.LoanProductService;


@RestController
@RequestMapping("/bigbank")
public class LoanProductController {
	
	@Autowired
	LoanProductService loanProductService;
	
	@PostMapping("/loanproducts")
    public LoanProduct addLoanProduct(@RequestBody LoanProduct loanProduct) {
        return loanProductService.addLoanProduct(loanProduct);
    }
	
	
	@PostMapping("/loanproducts/{loanProductid}")
    public LoanProduct updateLoanProduct(@PathVariable Integer loanProductId, @RequestBody LoanProduct loanProduct) {
        return loanProductService.updateLoanProduct(loanProductId, loanProduct);
    }
	
	
	@GetMapping("/loanproducts")
    public List<LoanProduct> getLoanProductsDetails() {
        return loanProductService.getLoanProductDetails();
    }
	
}
