package com.cts.blms.service;

public interface ReportService {

	byte[] generateLoanReport(Long id);

	byte[] generateRepaymentReport(Long id);

	byte[] generateOutStandingLoanReport();

	byte[] generateCustomerOutStandingLoanReport(Long id);

	byte[] getPortfolio(Long id);

}
