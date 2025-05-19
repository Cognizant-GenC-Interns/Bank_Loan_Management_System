package com.cts.blms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.cts.blms.model.Customer;
import com.cts.blms.service.CustomerService;

import jakarta.validation.Valid;


@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@PostMapping("/signup")
	public String registerCustomer(@Valid @ModelAttribute("customer") Customer customer,BindingResult result) {
		if(result.hasErrors()) {
			return "signup";
		}
		service.addCustomer(customer);
		return "home";
	}
	
	@GetMapping("/customerDetails")
	public String getCustomerDetails(Model model) {
		model.addAttribute("CustomerDetails",service.getCustomerDetails());
		return "customerDetails";
	}
	
	@PutMapping("/updateCustomer")
	public String updateCustomerProfile(@ModelAttribute("customer") Customer customer) {
		service.updateCustomerProfile(customer);
		return "home"; 

	}
}
