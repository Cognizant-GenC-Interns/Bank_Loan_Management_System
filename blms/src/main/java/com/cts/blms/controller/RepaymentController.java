package com.cts.blms.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.PaymentStatus;
import com.cts.blms.model.Repayment;
import com.cts.blms.service.LoanApplicationService;
import com.cts.blms.service.RepaymentService;

@Controller
@RequestMapping("/repayments")
public class RepaymentController {

	@Autowired
	private RepaymentService repaymentService;

	@Autowired
	private LoanApplicationService loanApplicationService;

	@PostMapping("/makePayment/{repaymentId}")
	public String makePayment(@PathVariable Long repaymentId, @RequestParam double amountPaid,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate) {
		repaymentService.makePayment(repaymentId, amountPaid, paymentDate);
		Repayment repayment=repaymentService.getRepayment(repaymentId);
		return "redirect:/repayments/paymentSchedule/"+repayment.getLoanApplication().getLoanApplicationId();

//		try {
//			
//			return new ResponseEntity<>("Payment processed successfully for repayment ID: " + repaymentId, HttpStatus.OK);
//		} catch (RuntimeException e) {
//			return new ResponseEntity<>("Error processing payment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//		} catch (Exception e) {
//			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
	}

	@GetMapping("/outstandingBalance/{loanApplicationId}")
	public ResponseEntity<String> getOutstandingBalance(@PathVariable Long loanApplicationId) {
		try {
			LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId); 
			if (loanApplication != null) {
				return new ResponseEntity<>("Loan Application not found with ID: " + loanApplicationId,
						HttpStatus.NOT_FOUND);
			}
			String outstandingBalance = repaymentService.getOutstandingBalance(loanApplication);
			return new ResponseEntity<>(outstandingBalance, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>("Error retrieving outstanding balance: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/paymentSchedule/{loanApplicationId}")
	public String getPaymentSchedule(@PathVariable Long loanApplicationId, Model model) {

		List<Repayment> repaymentDetails = repaymentService.getRepayementByLoanApplicationById(loanApplicationId)
				.stream().sorted(Comparator.comparing(Repayment::getDueDate)).toList();
		Map<String, Object> repay = new HashMap<>();
		repay.put("loanApplication", loanApplicationService.getLoanApplicationById(loanApplicationId));
		for (Repayment repayment : repaymentDetails) {
			System.out.println(repayment.getInterestPaid());
			System.out.println(repayment.getPaymentStatus());
		}
		repay.put("UpcomingDue",
				repaymentDetails.stream().filter(v -> v.getPaymentStatus().equals(PaymentStatus.PENDING)).toList());
		repay.put("CompletedDues",
				repaymentDetails.stream().filter(v -> v.getPaymentStatus().equals(PaymentStatus.COMPLETED))
						.sorted(Comparator.comparing(Repayment::getDueDate)).toList());
		model.addAllAttributes(repay);
		return "repayment";

	}

}
