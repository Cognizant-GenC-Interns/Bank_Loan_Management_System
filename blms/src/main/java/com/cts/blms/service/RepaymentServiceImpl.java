package com.cts.blms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for transactional integrity

import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.LoanApplicationStatus;
import com.cts.blms.model.PaymentStatus;
import com.cts.blms.model.Repayment;
import com.cts.blms.repository.LoanApplicationRepository;
import com.cts.blms.repository.RepaymentRepository;

@Service
public class RepaymentServiceImpl implements RepaymentService {

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;

	@Autowired
	private RepaymentRepository repaymentRepository;

	@Override
	public Date getPaymentSchedule(Long loanApplicationId) {
		LoanApplication application = loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new RuntimeException("Loan Application not found with ID: " + loanApplicationId));

		LocalDate applicationDate = application.getApplicationDate();
		LocalDate approvedDate = application.getApprovedDate();

		if (applicationDate == null || approvedDate == null) {
			throw new RuntimeException("Application or approval date is missing.");
		}

		LocalDate repaymentStartDate;

		if (!applicationDate.isBefore(approvedDate)) {
			repaymentStartDate = applicationDate.plusMonths(1);
		} else {
			repaymentStartDate = approvedDate.plusMonths(1);
		}

		return java.sql.Date.valueOf(repaymentStartDate);
	}

	@Override
	@Transactional
	public void makePayment(Long repaymentId, double amountPaid, LocalDate paymentDate) {

		
		
	    Repayment repayment = repaymentRepository.findById(repaymentId)
	        .orElseThrow(() -> new RuntimeException("Repayment not found with ID: " + repaymentId));

		double needToPay = 0.0;
		double interestPaid = 0.0;
		double principalPaid = 0.0;


	    LoanApplication loan = repayment.getLoanApplication();
	    
	  
	    if (loan.getLoanProduct() == null || loan.getLoanProduct().getTenure() == null || loan.getLoanProduct().getTenure() <= 0) {
	        throw new RuntimeException("Invalid loan product or tenure for loan ID: " + loan.getLoanApplicationId());
	    }
	    
	    if(loan.getMonthsRemaining()>0) {
		    loan.setMonthsRemaining(loan.getMonthsRemaining()-1);
		    
	    }else
		    {
		    	System.out.println("Asset seized...");
		    	return;
		    }

		double minPrincipal = repayment.getAmountDue();
		double minInterest = repayment.getInterestAmount();

		long daysLate = getDaysBetween(repayment.getDueDate(), paymentDate);
		double currentRepaymentFine = daysLate > 0 ? daysLate * 10 : 0.0;
		repayment.setFineAmount(currentRepaymentFine);
		System.out.println("FineAmount"+currentRepaymentFine+" daysLate :"+daysLate);
		// --- 1. Handle Fine ---
		if (amountPaid >= currentRepaymentFine) {
			amountPaid -= currentRepaymentFine;
		} else {
//cas2 2 :less than the fine amount
			needToPay += (currentRepaymentFine - amountPaid);
			amountPaid = 0.0;
			repayment.setPaymentStatus(PaymentStatus.PENDING);
			
			loan.setBalance(loan.getBalance() + needToPay);
			repaymentRepository.save(repayment);
			loanApplicationRepository.save(loan);
			return;
		}

		if (amountPaid >= minInterest) {
		    interestPaid = minInterest;
		    amountPaid -= minInterest;
		} else {
		    interestPaid = amountPaid;
		    needToPay += (minInterest - amountPaid);
		    amountPaid = 0.0;
		   

//			repaymentRepository.save(repayment);
//			loanApplicationRepository.save(loan);
//			return;
		}
		repayment.setInterestPaid(interestPaid);

//		repayment.setPrincipalPaid(amountPaid);
//		if (amountPaid > 0) {
		if (amountPaid >= minPrincipal) {
		    principalPaid = minPrincipal;
		    amountPaid -= minPrincipal;
		    loan.setBalance(loan.getBalance() - principalPaid);
		} else {
		    principalPaid = amountPaid;
		    needToPay += (minPrincipal - amountPaid);
		    loan.setBalance(loan.getBalance() - principalPaid);
		    amountPaid = 0.0;
		}

//		}
		repayment.setPrincipalPaid(principalPaid+amountPaid);
		double extraPaid=0;
	    if(amountPaid > 0)
	    {
	    	
	    	if(amountPaid >= minPrincipal)
	    	{
	    		//case 5 :more than on month
	    		extraPaid = minPrincipal; 
	    		loan.setBalance(loan.getBalance() - principalPaid); 
	    		amountPaid -= minPrincipal; 
	    		if (amountPaid > 0) { 
	    		    loan.setBalance(loan.getBalance() - amountPaid); 
	    		    System.out.println("Overpayment detected: " + amountPaid + " applied to loan balance.");
	    		}
	    	}
	    	else
	    	{
	    		extraPaid = amountPaid; 
	    		loan.setBalance(loan.getBalance() - principalPaid);  
	    		amountPaid = 0.0; 
	    	}
	    }

		if(loan.getBalance()<0) {
			loan.setBalance(0.0);
		}
		
		repayment.setPaymentDate(paymentDate);
		
		repayment.setPaymentStatus(PaymentStatus.COMPLETED);

		repaymentRepository.save(repayment);
		loanApplicationRepository.save(loan);
		if (loan.getBalance() <= 0.001) {
			System.out.println("Loan is Settled");
			loan.setLoanApplicationStatus(LoanApplicationStatus.SETTLED);
			loanApplicationRepository.save(loan);
			return;
		}

		else {
			Repayment nextRepayment = new Repayment();
			nextRepayment.setLoanApplication(loan);
			nextRepayment.setDueDate(repayment.getDueDate().plusMonths(1));
			nextRepayment.setPaymentStatus(PaymentStatus.PENDING);

			double remainingLoan = loan.getBalance();

			int remainingTenure = (int) loan.getMonthsRemaining();
			BigDecimal nextPrincipalAmount,nextInterestAmount;
			if (remainingTenure <= 0) {
				System.out.println("No more Scheduled repayments, loan should be settled");
				return;
				
			} else {
				
				if (remainingTenure > 0) {
				    double recalculatedPrincipal = loan.getBalance() / remainingTenure;
				    nextPrincipalAmount = BigDecimal.valueOf(recalculatedPrincipal + needToPay);
				} else {
				    System.out.println("Loan should be settled. No further repayments needed.");
				    return;
				}
				if (nextPrincipalAmount.doubleValue() < 0.01) {
				    nextPrincipalAmount = BigDecimal.ZERO;
				}


				nextInterestAmount = BigDecimal.valueOf(minInterest);

//				double nextInterestAmount = (remainingLoan * (loan.getLoanProduct().getInterestRate() / 100))
//						/ remainingTenure;
//				
//				double nextPrincipleAmount = remainingLoan / remainingTenure;

				
				
				BigDecimal roundedPrincipal = nextPrincipalAmount.setScale(2, RoundingMode.HALF_UP);
				BigDecimal roundedInterest = nextInterestAmount.setScale(2, RoundingMode.HALF_UP);

				nextRepayment.setAmountDue(roundedPrincipal.doubleValue());
				nextRepayment.setInterestAmount(roundedInterest.doubleValue());

				repaymentRepository.save(nextRepayment);
			}
		}
	}

	private long getDaysBetween(LocalDate from, LocalDate paymentDate) {
		return ChronoUnit.DAYS.between(from, paymentDate);
	}

	private long getMonthsBetween(LocalDate from, LocalDate to) {
		return ChronoUnit.MONTHS.between(from.withDayOfMonth(1), to.withDayOfMonth(1));
	}

	@Override
	public String getOutstandingBalance(LoanApplication loanApplication) {
		List<Repayment> repayments = repaymentRepository.findByLoanApplication(loanApplication);
		double totalPrincipalPaid = 0.0;
		double totalInterestPaid = 0.0;
		double totalFineAmount = 0.0;

		for (Repayment repayment : repayments) {
			if (repayment.getPrincipalPaid() != null) {
				totalPrincipalPaid += repayment.getPrincipalPaid();
			}
			if (repayment.getInterestPaid() != null) {
				totalInterestPaid += repayment.getInterestPaid();
			}
			if (repayment.getFineAmount() != null) {
				totalFineAmount += repayment.getFineAmount();
			}
		}

		double currentLoanPrincipal = loanApplication.getRequestAmount();
		double totalInterestDueOnCurrentPrincipal = calculateTotalInterest(loanApplication);

		double outstandingPrincipal = Math.max(0.0, currentLoanPrincipal - totalPrincipalPaid);
		double outstandingInterest = Math.max(0.0, totalInterestDueOnCurrentPrincipal - totalInterestPaid);
		double outstandingFine = Math.max(0.0, totalFineAmount);

		double totalOutstandingBalance = outstandingPrincipal + outstandingInterest + outstandingFine;

		return String.format("Outstanding Balance: %.2f (Principal: %.2f, Interest: %.2f, Fine: %.2f)",
				totalOutstandingBalance, outstandingPrincipal, outstandingInterest, outstandingFine);
	}

	@Override
	public void createRepayment(Repayment repayment) {
		repayment.setPaymentStatus(PaymentStatus.PENDING);
		System.out.println("create Repayment : "+repayment.getLoanApplication().getRequestAmount());
		BigDecimal interestAmount = BigDecimal.valueOf(calculateTotalInterest(repayment.getLoanApplication()) /
				repayment.getLoanApplication().getLoanProduct().getTenure());
		interestAmount = interestAmount.setScale(2, RoundingMode.HALF_UP); // 2 decimal places

		repayment.setInterestAmount(interestAmount.doubleValue());

		System.out.println("Interest Amount per month:"+repayment.getInterestAmount());
		repaymentRepository.save(repayment);
	}

	private double calculateTotalInterest(LoanApplication loanApplication) {
		
	    if (loanApplication.getLoanProduct() == null || loanApplication.getLoanProduct().getTenure() == null || loanApplication.getLoanProduct().getTenure() <= 0 || loanApplication.getLoanProduct().getInterestRate() == null) 
	    {
	        return 0.0;
	    }  
	    System.out.println("Entering calc total Interest:");
	    double principal=loanApplication.getRequestAmount(); 
	    double tenure = loanApplication.getLoanProduct().getTenure();
	    double interestRate = loanApplication.getLoanProduct().getInterestRate();
//	    System.out.println(principal);
//	    System.out.println((double)tenure/12);
//	    System.out.println(interestRate);
		   
	    System.out.println(principal *(double)(tenure/12)* (double)interestRate/100);
//	    return (principal *(tenure/12)* interestRate/100) ;
	    return principal *(double)(tenure/12)* (double)interestRate/100;
	}

	@Override
	public List<Repayment> getRepayementByLoanApplicationById(Long loanApplicationId) {
		LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).get();

		List<Repayment> repayments = repaymentRepository.findByLoanApplication(loanApplication);
		return repayments;

		// TODO Auto-generated method stub

	}

	@Override
	public Repayment getRepayment(Long repaymentId) {
		// TODO Auto-generated method stub
		return repaymentRepository.findById(repaymentId).get();
	}
}
