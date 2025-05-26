package com.cts.blms.controller;
import java.io.IOException;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
 
import com.cts.blms.model.LoanApplication;
import com.cts.blms.service.LoanApplicationService;
 
import jakarta.validation.Valid;
 
 
@Controller
public class LoanApplicationController {
	@Autowired
	LoanApplicationService loanApplicationService;
	@PostMapping("/applyLoan")
	public String loanApply(@Valid @ModelAttribute("loanApplication") LoanApplication loanApplication,@RequestParam("assetimage") MultipartFile assetImage,BindingResult result) throws IOException {
		if(result.hasErrors()) {
			return "redirect:/";
		}
 
		if (!assetImage.isEmpty()) {
			loanApplication.setAssetImage(assetImage.getBytes());
			loanApplication.setAssetName(assetImage.getOriginalFilename());
			}
		loanApplicationService.addLoanApplication(loanApplication);
		return "appliedSuccesfully";
	}

}