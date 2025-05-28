package com.cts.blms.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.LoanApplicationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class LoanApplicationController {
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	
	@PostMapping("/saveAppliedLoan")
	public String loanApply(@Valid @ModelAttribute("loanApplication") LoanApplication loanApplication,@RequestParam("assetFile") MultipartFile assetImage,HttpSession session,BindingResult result,Model model) throws IOException {
		if(result.hasErrors()) {
			return "LoanApplicationForm";
		}
		Customer customer=(Customer) session.getAttribute("loggedCustomer");
		LoanProduct loanProduct=(LoanProduct) session.getAttribute("selectedLoanProduct");
		
		if (!assetImage.isEmpty()) {
			loanApplication.setAssetImage(assetImage.getBytes());
			loanApplication.setAssetName(assetImage.getOriginalFilename());
			}
		
		
		
		
		double monthlySalary = customer.getAnnualSalary() / 12.0;
        double maxAffordableAmount = monthlySalary * 0.4;
        double principle=loanApplication.getRequestAmount();
        double monthlyIntrestRate=loanProduct.getInterestRate()/12/100;
        double ratePower=Math.pow(1+monthlyIntrestRate,loanProduct.getTenure());
        double emiAmount=(principle*monthlyIntrestRate*ratePower)/(ratePower-1);
        
        loanApplication.setMaxAffordableAmount(maxAffordableAmount);
        loanApplication.setEmiAmount(emiAmount);
        loanApplication.setCustomer(customer);
        loanApplication.setLoanProduct(loanProduct);
		
		
		loanApplicationService.addLoanApplication(loanApplication);
		model.addAttribute("loanApplication",loanApplication);
		
		return "redirect:/user/userDashboard";
	}
	
	@GetMapping("/appliedLoans")
    public String getLoanProductsDetails(Model model,HttpSession session) {
		Customer customer=(Customer) session.getAttribute("loggedCustomer");
        List<LoanApplication>appliedLoans=loanApplicationService.getLoanApplicationByCustomer(customer);
        model.addAttribute("appliedLoans", appliedLoans);
        
        return "userDashboard";
        
    }
	
}
