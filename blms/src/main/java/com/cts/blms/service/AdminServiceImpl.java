package com.cts.blms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Customer;
import com.cts.blms.repository.CustomerRepository;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private CustomerRepository customerRepository;
	
	
	@Override
	public List<Customer> getCustomerDetails() {
		return customerRepository.findAll();
	}

}
