package com.cts.blms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cts.blms.model.Customer;
import com.cts.blms.model.KycStatus;
import com.cts.blms.repository.CustomerRepository;

import jakarta.validation.Valid;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository repository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public Customer addCustomer(@Valid Customer customer) {		// TODO Auto-generated method stub
		customer.setKycStatus(KycStatus.PENDING);
		int creditScore = new Random().nextInt(901);
		customer.setCreditScore(creditScore);
		if(repository.findByEmail(customer.getEmail()) != null) {
			return customer;
		}

		String hashedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(hashedPassword);
	 
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
		if(customer!=null && passwordEncoder.matches(password, customer.getPassword())) {
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

	@Override
	public List<Customer> getVerifiedCustomer() {
		// TODO Auto-generated method stub
		List<Customer> verifiedCust=repository.findAll().stream().filter(v->v.getKycStatus().equals(KycStatus.VERIFIED)).toList();
		System.out.println("Verified Customer :");
		for (Customer customer : verifiedCust) {
			System.out.println(customer.getCustomerId());
		}
		return verifiedCust;
	}

	@Override
	public List<Customer> getPendingCustomer() {
		// TODO Auto-generated method stub
		List<Customer> pendingCust=repository.findAll().stream().filter(v->v.getKycStatus().equals(KycStatus.PENDING)).toList();
		System.out.println("Pending Customer :");
		for (Customer customer : pendingCust) {
			System.out.println(customer.getCustomerId());
		}
		return pendingCust;
	}

	
}
