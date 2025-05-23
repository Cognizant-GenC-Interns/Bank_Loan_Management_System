package com.cts.blms.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.blms.model.Admin;
import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanProduct;
import com.cts.blms.service.AdminService;
import com.cts.blms.service.CustomerService;
import com.cts.blms.service.LoanProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private LoanProductService loanProductService;
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/adminDashboard")
	public String Dashboard(Model model,HttpSession session) {
		
		Admin admin=(Admin)session.getAttribute("admin");
		System.out.println("Admin email:"+admin.getEmail());
		Map<String,Object> dataForAdmin=new HashMap<>();
		dataForAdmin.put("verifiedCustomer",customerService.getVerifiedCustomer() );
		dataForAdmin.put("pendingCustomer",customerService.getPendingCustomer());
		dataForAdmin.put("loanProducts",loanProductService.getLoanProductDetails());
		dataForAdmin.put("newLoanProduct",new LoanProduct());
		dataForAdmin.put("admin", admin);
		model.addAllAttributes(dataForAdmin);
		return "adminDashboard";
	}
	
	@PostMapping("/adminLogin")
	public String adminLogin(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session) {
			if(adminService.validateAdmin(email,password)) {
				Admin admin = adminService.findAdmin(email);
				session.setAttribute("admin", admin);
				return "redirect:/admin/adminDashboard";
			}
			
			
			return "redirect:/";
		}
	
	@PostMapping("/updateKyc/{customerId}")
	public String updateKycStatus(@PathVariable("customerId") long id,Model model) {
		System.out.println("customer id:" + id);
		Customer customer=customerService.updateKycStatus(id);
		model.addAttribute("updateCustomer", customer);
		return "redirect:/admin/adminDashboard";
	}
}
