package com.cts.blms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cts.blms.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/adminDashboard")
	public String Dashboard(Model model) {
		model.addAttribute("Customer",adminService.getCustomerDetails());
		return "adminDashboard";
	}
}
