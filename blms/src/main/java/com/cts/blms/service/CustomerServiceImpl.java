package com.cts.blms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Customer;
import com.cts.blms.repository.CustomerRepository;

import jakarta.validation.Valid;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository repository;

	@Override
	public Customer addCustomer(@Valid Customer customer) {
		return repository.save(customer);
	}

	@Override
	public List<Customer> getCustomerDetails() {
		return repository.findAll();
	}

	@Override
	public Customer updateCustomerProfile(Customer customer) {
		return repository.save(customer);
	}

}
