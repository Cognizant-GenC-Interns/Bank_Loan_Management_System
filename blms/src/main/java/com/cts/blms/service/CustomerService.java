package com.cts.blms.service;

import java.util.List;

import com.cts.blms.model.Customer;

import jakarta.validation.Valid;

public interface CustomerService {

	Customer addCustomer(@Valid Customer customer);
	
	List<Customer> getCustomerDetails();

	Customer updateCustomerProfile(Customer customer);

}
