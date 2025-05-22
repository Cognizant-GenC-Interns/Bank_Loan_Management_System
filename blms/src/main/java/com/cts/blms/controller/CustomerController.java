package com.cts.blms.controller;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.CustomerService;
import com.cts.blms.service.LoanProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class CustomerController {
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
	@GetMapping("/userDashboard")
	public String welcome(Model model) {
		model.addAttribute("loanProducts",loanProductService.getLoanProductDetails());
		return "welcome";
	}
	
	@PostMapping("/adminLogin")
	public String adminLogin(@RequestParam("email")String email,@RequestParam("password")String password,Model model) {
		
//		if(email.equals("$") && password.equals("${admin.password}")) {
//			model.addAttribute("customer", customer);
		
			logger.debug("Admin logged in");
			
			return "redirect:/adminDashboard";
//		}
//			return "redirect:/";
	}
	
	
	@GetMapping("/adminDashboard")
	public String Dashboard(Model model) {
		model.addAttribute("Customer",customerService.getCustomerDetails());
		model.addAttribute("newLoanProduct",new LoanProduct());
		return "adminDashboard";
	}
	
	@PostMapping("/signup")
	public String registerCustomer(@Valid @ModelAttribute("customer")  Customer customer,BindingResult result) {
		if(result.hasErrors()) {
			
			return "redirect:/";
		}
		customerService.addCustomer(customer);
		return "redirect:/";
	}
	

	@GetMapping("/welcome")
	public String welcome(HttpSession session, Model model) {
	    Customer customer = (Customer) session.getAttribute("loggedCustomer");
	   
	    
	    if (customer != null) {
	        model.addAttribute("loggedCustomer", customer);
	        model.addAttribute("editCustomer",new Customer());
	        
	        return "welcome";
	    }
	    return "redirect:/";
	}


	@PostMapping("/login")
	public String customerLogin(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session) {
		Customer customer=customerService.validateCustomer(email,password);

		if(customer!=null) {	
		session.setAttribute("loggedCustomer", customer);
			return "redirect:/welcome";
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
	        return "redirect:/";
	        }
	    Customer updatedCust=customerService.updateCustomerProfile(customer);
	    session.setAttribute("loggedCustomer", updatedCust);
	    return "redirect:/welcome";
	}
	
	@PostMapping("/updateKyc/{customerId}")
	public String updateKycStatus(@PathVariable("customerId") long id,Model model) {
		System.out.println("customer id:" + id);
		Customer customer=customerService.updateKycStatus(id);
		model.addAttribute("updateCustomer", customer);
		return "redirect:/adminDashboard";
	}
}
