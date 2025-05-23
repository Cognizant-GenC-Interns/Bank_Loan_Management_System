package com.cts.blms.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.blms.model.Customer;
import com.cts.blms.service.CustomerService;
import com.cts.blms.service.LoanProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private LoanProductService loanProductService;
	
	
	@GetMapping("/userDashboard")
	public String welcome(HttpSession session, Model model) {
	    Customer customer = (Customer) session.getAttribute("loggedCustomer"); 
	    if (customer != null) {
	        model.addAttribute("loggedCustomer", customer);    
	        model.addAttribute("loanProducts",loanProductService.getLoanProductDetails());  
	        return "userDashboard";
	    }
	    return "redirect:/";
	}


	
	
	@GetMapping("/customerDetailsById")
	public String getCustomerDetailsById(@RequestParam("id") long id,Model model) {
		Customer customer=customerService.getCustomerDetailsById(id);
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
	
	@PostMapping("/updateCustomer")
	public String updateCustomerProfile(@Valid @ModelAttribute("loggedCustomer") Customer customer, BindingResult result, HttpSession session) {
	    if (result.hasErrors()) {
	        return "redirect:/user/userDashboard";
	        }
	    Customer updatedCust=customerService.updateCustomerProfile(customer);
	    session.setAttribute("loggedCustomer", updatedCust);
	    return "redirect:/user/userDashboard";
	}
	
	
}
