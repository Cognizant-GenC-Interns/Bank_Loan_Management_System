package com.cts.blms.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanApplicationStatus;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.LoanApplicationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/loans")
public class LoanApplicationController {

	@Autowired
	private LoanApplicationService loanApplicationService1;

	@PostMapping("/approveLoan")
	public String approveLoan(@RequestParam("loanId") Long id, @RequestParam("approvedDate") LocalDate approveDate) {
		loanApplicationService1.approveLoan(id, approveDate);
		return "redirect:/loans/appliedLoans";//

	}

	@PostMapping("/rejectLoan")
	public String rejectLoan(@RequestParam("loanId") Long id) {
		loanApplicationService1.rejectLoan(id);
		return "redirect:/loans/appliedLoans";

	}

	@GetMapping("/assetImage/{id}")
	public ResponseEntity<byte[]> getSalarySlipImage(@PathVariable Long id) {
		LoanApplication loanApplication = loanApplicationService1.getLoanApplicationById(id);

		if (loanApplication != null && loanApplication.getAssetImage() != null) {

			// Guess MIME type from file name
			String mimeType = URLConnection.guessContentTypeFromName(loanApplication.getAssetName());
			if (mimeType == null) {
				mimeType = "application/octet-stream"; // Fallback if unknown
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
					.body(loanApplication.getAssetImage());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/saveAppliedLoan")
	public String loanApply(@Valid @ModelAttribute("loanApplication") LoanApplication loanApplication,
			@RequestParam("assetFile") MultipartFile assetImage, HttpSession session, BindingResult result, Model model)
			throws IOException {
		if (result.hasErrors()) {
			return "LoanApplicationForm";
		}
		Customer customer = (Customer) session.getAttribute("loggedCustomer");
		LoanProduct loanProduct = (LoanProduct) session.getAttribute("selectedLoanProduct");

		if (!assetImage.isEmpty()) {
			loanApplication.setAssetImage(assetImage.getBytes());
			loanApplication.setAssetName(assetImage.getOriginalFilename());
		}
		loanApplication = loanApplicationService1.getEligibility(customer, loanProduct, loanApplication);

		loanApplicationService1.addLoanApplication(loanApplication);
		model.addAttribute("loanApplication", loanApplication);

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
	public String deleteLoan(@PathVariable long loanApplicationId) {
		loanApplicationService1.deleteLoan(loanApplicationId);
		return "redirect:/user/userDashboard";
	}

	@GetMapping("/appliedLoans")
	public String getLoanProductsDetails(Model model, HttpSession session) {
		List<LoanApplication> appliedLoans = loanApplicationService1.getAllLoanapplicationDetails();
		List<LoanApplication> requestPendingLoans = null, approvedLoans = null;
		if (!appliedLoans.isEmpty()) {
			requestPendingLoans = appliedLoans.stream()
					.filter(a -> a.getLoanApplicationStatus() == LoanApplicationStatus.PENDING).toList();
			approvedLoans = appliedLoans.stream()
					.filter(a -> a.getLoanApplicationStatus() == LoanApplicationStatus.APPROVED).toList();

		}

		model.addAttribute("requestPendingLoans", requestPendingLoans);
		model.addAttribute("approvedLoans", approvedLoans);

		return "adminLoanHandler";// return "//";

	}

}