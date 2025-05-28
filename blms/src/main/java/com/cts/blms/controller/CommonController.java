package com.cts.blms.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.cts.blms.service.CustomerService;
import com.cts.blms.service.LoanProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CommonController {
	private static final  Logger logger = LogManager.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private LoanProductService loanProductService;
	
	@GetMapping("/")
	public String home(Model model) {
		Customer customer=new Customer();
		model.addAttribute("customer",customer);
		model.addAttribute("loanProducts",loanProductService.getName());
		logger.info("Directed to Home page ");
		logger.debug("Null value"+customer);
		return "home";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return "redirect:/";
	}
	
	
	@GetMapping("/invalidCredentials")
	public String invalidData() {
		return "InvalidCredentials";
	}
	
	
	

	
}
