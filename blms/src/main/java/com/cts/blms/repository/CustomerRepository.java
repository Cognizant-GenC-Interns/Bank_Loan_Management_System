package com.cts.blms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.blms.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByEmail(String email);

}
