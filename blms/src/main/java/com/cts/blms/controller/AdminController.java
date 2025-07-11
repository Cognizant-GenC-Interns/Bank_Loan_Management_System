package com.cts.blms.controller;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.blms.entity.Admin;
import com.cts.blms.entity.Customer;
import com.cts.blms.entity.LoanProduct;
import com.cts.blms.service.AdminService;
import com.cts.blms.service.CustomerService;
import com.cts.blms.service.LoanApplicationService;
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

	@Autowired
	private LoanApplicationService loanApplicationService;

	@GetMapping("/adminDashboard")
	public String Dashboard(Model model, HttpSession session) {

		Admin admin = (Admin) session.getAttribute("admin");
		Map<String, Object> dataForAdmin = new HashMap<>();
		dataForAdmin.put("verifiedCustomer", customerService.getVerifiedCustomer());
		dataForAdmin.put("pendingCustomer", customerService.getPendingCustomer());
		dataForAdmin.put("loanProducts", loanProductService.getLoanProductDetails());
		dataForAdmin.put("newLoanProduct", new LoanProduct());
		dataForAdmin.put("appliedLoans", loanApplicationService.getAllLoanapplicationDetails());
		dataForAdmin.put("admin", admin);
		model.addAllAttributes(dataForAdmin);
		return "adminDashboard";
	}

	@PostMapping("/adminLogin")
	public String adminLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {
		if (adminService.validateAdmin(email, password)) {
			Admin admin = adminService.findAdmin(email);
			session.setAttribute("admin", admin);
			return "redirect:/admin/adminDashboard";
		}

		return "redirect:/invalidCredentials";
	}

	@PostMapping("/updateKyc/{customerId}")
	public String updateKycStatus(@PathVariable("customerId") long id, Model model) {
		System.out.println("customer id:" + id);
		Customer customer = customerService.updateKycStatus(id);
		model.addAttribute("updateCustomer", customer);
		return "redirect:/admin/adminDashboard";
	}

	@GetMapping("/images/pan/{id}")
	public ResponseEntity<byte[]> getPanCardImage(@PathVariable Long id) {
		Customer customer = customerService.getCustomerDetailsById(id);

		if (customer != null && customer.getPanImage() != null) {
			String mimeType = URLConnection.guessContentTypeFromName(customer.getPanImageName());
			if (mimeType == null) {
				mimeType = "application/octet-stream"; // Fallback if unknown
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(customer.getPanImage());
		}
		return ResponseEntity.notFound().build();

	}

	@GetMapping("/images/salary/{id}")
	public ResponseEntity<byte[]> getSalarySlipImage(@PathVariable Long id) {
		Customer customer = customerService.getCustomerDetailsById(id);

		if (customer != null && customer.getSalarySlipImage() != null) {
			String mimeType = URLConnection.guessContentTypeFromName(customer.getSalarySlipImageName());
			if (mimeType == null) {
				mimeType = "application/octet-stream"; // Fallback if unknown
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
					.body(customer.getSalarySlipImage());
		}
		return ResponseEntity.notFound().build();
	}

}
