package com.cts.blms.controller;
import java.io.IOException;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.LoanApplicationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


 
@Controller
public class LoanApplicationController {
	
	@Autowired
	private LoanApplicationService loanApplicationService1;
	
	
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
		loanApplication=loanApplicationService1.getEligibility(customer,loanProduct,loanApplication);
		
		loanApplicationService1.addLoanApplication(loanApplication);
		model.addAttribute("loanApplication",loanApplication);
		
		return "redirect:/user/userDashboard";
	}
//	
//	@GetMapping("/appliedLoans")
//    public String getLoanProductsDetails(Model model,HttpSession session) {
//		Customer customer=(Customer) session.getAttribute("loggedCustomer");
//        List<LoanApplication>appliedLoans=loanApplicationService.getLoanApplicationByCustomer(customer);
//        model.addAttribute("appliedLoans", appliedLoans);
//        
//        return "userDashboard";
//        
//    }
	
	@PostMapping("/deleteLoan/{loanApplicationId}")
	public String deleteLoan(@PathVariable long loanApplicationId, RedirectAttributes redirectAttributes) {
		loanApplicationService1.deleteLoan(loanApplicationId);
		return "redirect:/user/userDashboard";
	}

}