package com.cts.blms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.cts.blms.model.Customer;

import jakarta.validation.Valid;

public interface CustomerService {

	Customer addCustomer(@Valid Customer customer, MultipartFile panCard, MultipartFile salarySlip) throws IOException;

	Customer updateCustomerProfile(Customer customer);
	List<Customer> getCustomerDetails();

	Customer validateCustomer(String email, String password);

	Customer getCustomerDetailsById(long id);

	Customer updateKycStatus(long id);

	List<Customer> getVerifiedCustomer();

	List<Customer> getPendingCustomer();

	

	
}
