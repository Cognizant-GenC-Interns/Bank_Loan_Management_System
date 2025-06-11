package com.cts.blms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.blms.entity.LoanProduct;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Long> {

}
