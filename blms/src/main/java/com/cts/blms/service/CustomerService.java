package com.cts.blms.service;

import java.util.List;
import java.util.Optional;

import com.cts.blms.model.Customer;

import jakarta.validation.Valid;

public interface CustomerService {

	Customer addCustomer(@Valid Customer customer);

	Customer updateCustomerProfile(Customer customer);

	Customer validateCustomer(String email, String password);

	Customer getCustomerDetailsById(long id);
}
