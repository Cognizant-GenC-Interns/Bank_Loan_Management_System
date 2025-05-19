package com.cts.blms.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.blms.model.Customer;
import com.cts.blms.service.CustomerService;

import jakarta.validation.Valid;


@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@GetMapping("/")
	public String home(Model model) {
		Customer customer=new Customer();
		model.addAttribute("customer",customer);
		return "home";
	}
	

	
	@PostMapping("/signup")
	public String registerCustomer(@Valid @ModelAttribute("customer")  Customer customer,BindingResult result) {
		System.out.println(customer.getEmail());
		if(result.hasErrors()) {
			
			return "redirect:/";
		}
		service.addCustomer(customer);
		return "redirect:/";
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@PostMapping("/login")
	public String customerLogin(@RequestParam("email")String email,@RequestParam("password")String password,Model model) {
		Customer customer=service.validateCustomer(email,password);
		System.out.println("");
		if(customer!=null) {
			model.addAttribute("customer", customer);
			return "redirect:/welcome";
		}
			return "redirect:/";
	}
	
	@GetMapping("/customerDetailsById")
	public String getCustomerDetailsById(@RequestParam("id") long id,Model model) {
		Customer customer=service.getCustomerDetailsById(id);
		if(customer!=null) {
			model.addAttribute("customer", customer);
			return "customerDetails";
		}
		return "customerDetailsById";
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
