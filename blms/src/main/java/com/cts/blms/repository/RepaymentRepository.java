package com.cts.blms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.blms.entity.LoanApplication;
import com.cts.blms.entity.Repayment;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

	List<Repayment> findByLoanApplication(LoanApplication loanApplication);

}
