package com.cts.blms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cts.blms.service.ReportService;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@ResponseBody
	@GetMapping("/loanReport/{id}")
	public ResponseEntity<ByteArrayResource> generateLoanReport(@PathVariable Long id) {
		byte[] pdfBytes = reportService.generateLoanReport(id);
		ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Loan_Report.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(resource);

	}

	@ResponseBody
	@GetMapping("/repaymentReport/{id}")
	public ResponseEntity<ByteArrayResource> generateRepaymentReport(@PathVariable Long id) {
		byte[] pdfBytes = reportService.generateRepaymentReport(id);
		ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Repayment_Report.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(resource);
	}

	@GetMapping("/getAllOutsatndingLoans")
	public ResponseEntity<ByteArrayResource> getAllOutstandingLoans() {
		byte[] pdfBytes = reportService.generateOutStandingLoanReport();
		ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Outstanding_Loans.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(resource);
	}

	@GetMapping("/getCustomerOutstandingLoan/{id}")
	public ResponseEntity<ByteArrayResource> getCustomerOutstandingLoans(@PathVariable Long id) {
		byte[] pdfBytes = reportService.generateCustomerOutStandingLoanReport(id);
		ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=OutStanding_Loans_Of_Customer.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(resource);
	}

	@GetMapping("/getPortfolio/{id}")
	public ResponseEntity<ByteArrayResource> getCustomerPortfolio(@PathVariable Long id) {
		byte[] pdfBytes = reportService.getPortfolio(id);
		ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(resource);
	}

}
