package com.cts.blms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.blms.model.Customer;
import com.cts.blms.model.LoanApplication;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>{

	List<LoanApplication> findByCustomer(Customer customer);
	
}
