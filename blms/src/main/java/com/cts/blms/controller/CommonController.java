package com.cts.blms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.blms.model.Customer;
import com.cts.blms.service.LoanProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommonController {
	private static final Logger logger = LogManager.getLogger(CustomerController.class);

	@Autowired
	private LoanProductService loanProductService;

	@GetMapping("/")
	public String home(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		model.addAttribute("loanProducts", loanProductService.getName());
		logger.info("Directed to Home page ");
		return "home";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		logger.info("User Logged out");
		return "redirect:/";
	}

	@GetMapping("/invalidCredentials")
	public String invalidData() {
		logger.info("Invalid Credentials");
		return "InvalidCredentials";
	}

}
