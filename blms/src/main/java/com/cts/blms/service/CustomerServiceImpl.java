package com.cts.blms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.model.Customer;
import com.cts.blms.model.KycStatus;
import com.cts.blms.repository.CustomerRepository;

import jakarta.validation.Valid;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository repository;

	@Override
	public Customer addCustomer(@Valid Customer customer) {
		customer.setKycStatus(KycStatus.PENDING);
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

	@Override
	public Customer validateCustomer(String email, String password) {
		Customer customer=repository.findByEmail(email);
		if(customer!=null && customer.getPassword().equals(password)) {
			return customer;
		}
		return null;
	}

	@Override
	public Customer getCustomerDetailsById(long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Customer updateKycStatus(long id) {
		Customer customer=repository.findById(id).orElse(null);
		System.out.println("Customer Status"+customer.getKycStatus());
		customer.setKycStatus(KycStatus.VERIFIED);
		return repository.save(customer);
	}
}
