package com.cts.blms.controller;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	private static final  Logger logger = LogManager.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService service;
	
	@GetMapping("/")
	public String home(Model model) {
		Customer customer=new Customer();
		model.addAttribute("customer",customer);
		logger.info("Directed to Home page ");
		logger.debug("Null value"+customer);
		return "home";
	}
	@GetMapping("/userDashboard")
	public String welcome() {
		return "welcome";
	}
	
	@PostMapping("/adminLogin")
	public String adminLogin(@RequestParam("email")String email,@RequestParam("password")String password,Model model) {
		
//		if(email.equals("$") && password.equals("${admin.password}")) {
//			model.addAttribute("customer", customer);
		
			logger.debug("Admin logged in");
			return "redirect:admin/adminDashboard";
//		}
//			return "redirect:/";
	}
	
//	@GetMapping("/adminDashboard")
//	public String Dashboard(Model model) {
//		model.addAttribute("Customer",service.getCustomerDetails());
//		return "adminDashboard";
//	}
	
	@PostMapping("/signup")
	public String registerCustomer(@Valid @ModelAttribute("customer")  Customer customer,BindingResult result) {
		System.out.println(customer.getEmail());
		if(result.hasErrors()) {
			
			return "redirect:/";
		}
		service.addCustomer(customer);
		return "redirect:/";
	}
	
	
	
	@PostMapping("/login")
	public String customerLogin(@RequestParam("email")String email,@RequestParam("password")String password,Model model) {
		Customer customer=service.validateCustomer(email,password);
	
		if(customer!=null) {
			model.addAttribute("customer", customer);
			return "redirect:/userDashboard";
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
	
	
//	@GetMapping("/customerDetails")
//	public String getCustomerDetails(Model model) {
//		model.addAttribute("CustomerDetails",service.getCustomerDetails());
//		return "customerDetails";
//	}
	
	@PutMapping("/updateCustomer")
	public String updateCustomerProfile(@ModelAttribute("customer") Customer customer) {
		service.updateCustomerProfile(customer);
		return "home"; 

	}
}
