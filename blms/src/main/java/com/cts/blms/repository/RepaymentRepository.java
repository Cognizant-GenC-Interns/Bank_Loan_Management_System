package com.cts.blms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.blms.model.Repayment;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long>{

}
