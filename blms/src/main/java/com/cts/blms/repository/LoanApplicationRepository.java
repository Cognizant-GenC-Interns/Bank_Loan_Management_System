package com.cts.blms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.blms.model.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>{

}
