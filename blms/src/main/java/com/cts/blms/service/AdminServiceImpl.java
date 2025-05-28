package com.cts.blms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Admin;
import com.cts.blms.model.Customer;
import com.cts.blms.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminRepository adminRepository;
	@Override
	public boolean validateAdmin(String email, String password) {
		// TODO Auto-generated method stub
		Admin admin=adminRepository.findByEmail(email);
		if(admin!=null) {
			return password.equals(admin.getPassword());
		}
		return false;
	}

	@Override
	public Admin findAdmin(String email) {
		// TODO Auto-generated method stub
		return adminRepository.findByEmail(email);
	}

}
