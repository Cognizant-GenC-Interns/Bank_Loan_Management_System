package com.cts.blms.service;

import java.util.List;

import com.cts.blms.model.Customer;

import jakarta.validation.Valid;

public interface CustomerService {

	Customer addCustomer(@Valid Customer customer);

	Customer updateCustomerProfile(Customer customer);

	List<Customer> getCustomerDetails();

	Customer validateCustomer(String email, String password);

	Customer getCustomerDetailsById(long id);

	Customer updateKycStatus(long id);

	List<Customer> getVerifiedCustomer();

	List<Customer> getPendingCustomer();

}
