package com.cts.blms.controller;



import java.io.IOException;
import java.net.URLConnection;
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

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.CustomerService;
import com.cts.blms.service.LoanApplicationService;
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
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@PostMapping("/signup")
	public String registerCustomer(@Valid @ModelAttribute("customer")  Customer customer,@RequestParam("panCard") MultipartFile panCard,@RequestParam("profile_image") MultipartFile profileImage,@RequestParam("salarySlip") MultipartFile salarySlip,BindingResult result) throws IOException {
		if(result.hasErrors()) {
			
			return "redirect:/";
		}

		if (!profileImage.isEmpty()) {
			customer.setProfileImage(profileImage.getBytes());
			customer.setProfileImageName(profileImage.getOriginalFilename());
	}

	if (!panCard.isEmpty()) {
		customer.setPanImage(panCard.getBytes());
		customer.setPanImageName(panCard.getOriginalFilename());
	}

	if (!salarySlip.isEmpty()) {
		customer.setSalarySlipImage(salarySlip.getBytes());
		customer.setSalarySlipImageName(salarySlip.getOriginalFilename());
	}

	customerService.addCustomer(customer);
			
		
		return "redirect:/";
	}
	
	
	@PostMapping("/login")
	public String customerLogin(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session) {
		Customer customer=customerService.validateCustomer(email,password);

		if(customer!=null) {	
		session.setAttribute("loggedCustomer", customer);
			return "redirect:/user/userDashboard";
		}
		return "redirect:/invalidCredentials";
	}
	
	
	@GetMapping("/userDashboard")
	public String welcome(HttpSession session, Model model) {
	    Customer customer = (Customer) session.getAttribute("loggedCustomer"); 
	    if (customer != null) {
	        model.addAttribute("loggedCustomer", customer);    
	        model.addAttribute("loanProducts",loanProductService.getLoanProductDetails());
	        List<LoanApplication>appliedLoans=loanApplicationService.getLoanApplicationByCustomer(customer);
	        model.addAttribute("appliedLoans", appliedLoans);

	        
	        return "userDashboard";
	    }
	   	    return "redirect:/";
	}
	
	
	@PostMapping("/updateCustomer")
	public String updateCustomerProfile(@Valid @ModelAttribute("loggedCustomer") Customer customer, BindingResult result, HttpSession session) {
	    if (result.hasErrors()) {
	        return "redirect:/user/userDashboard";
	        }
	    Customer updatedCust=customerService.updateCustomerProfile(customer);
	    session.setAttribute("loggedCustomer", updatedCust);
	    return "redirect:/user/userDashboard";
	}
	
	
	@GetMapping("/profile/{id}")
	public ResponseEntity<byte[]> getSalarySlipImage(@PathVariable Long id) {
	    Customer customer = customerService.getCustomerDetailsById(id);

	    if (customer == null || customer.getProfileImage() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Guess MIME type from file name
	    String mimeType = URLConnection.guessContentTypeFromName(customer.getProfileImageName());
	    if (mimeType == null) {
	        mimeType = "application/octet-stream"; // Fallback if unknown
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(mimeType))
	            .body(customer.getProfileImage());
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
	
	@GetMapping("/applyLoan")
	public String applyLoan(@RequestParam("productId") long loanProductId,Model model,HttpSession session) {
		LoanProduct selectedLoanProduct=loanProductService.getLoanProductById(loanProductId);
		if(selectedLoanProduct!=null) {
			session.setAttribute("selectedLoanProduct", selectedLoanProduct);
		}
		model.addAttribute("loanApplication",new LoanApplication());
		return "applyLoan";
	}
	
}
