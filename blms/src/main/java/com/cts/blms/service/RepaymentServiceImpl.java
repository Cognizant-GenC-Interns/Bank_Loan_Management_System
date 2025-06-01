package com.cts.blms.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for transactional integrity

import com.cts.blms.model.LoanApplication;
import com.cts.blms.model.PaymentStatus;
import com.cts.blms.model.Repayment;
import com.cts.blms.repository.LoanApplicationRepository;
import com.cts.blms.repository.RepaymentRepository;

@Service
public class RepaymentServiceImpl implements RepaymentService{
	
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

	    double needToPay=0.0;
	    double interestPaid=0.0;
	    double principalPaid=0.0;

	    LoanApplication loan = repayment.getLoanApplication();

	    if (loan.getLoanProduct() == null || loan.getLoanProduct().getTenure() == null || loan.getLoanProduct().getTenure() <= 0) {
	        throw new RuntimeException("Invalid loan product or tenure for loan ID: " + loan.getLoanApplicationId());
	    }

	    double minPrincipal = repayment.getAmountDue();
	    double minInterest = repayment.getInterestAmount();


	    long daysLate = getDaysBetween(repayment.getDueDate(), paymentDate);
	    double currentRepaymentFine = daysLate > 0 ? daysLate * 10 : 0.0;
	    repayment.setFineAmount(currentRepaymentFine);

	    // --- 1. Handle Fine ---
	    if(amountPaid >= currentRepaymentFine)
	    {
	    	amountPaid -= currentRepaymentFine; 
	    }
	    else
	    {
	    	
	    	needToPay += (currentRepaymentFine - amountPaid);
	    	amountPaid = 0.0; 
	    	repayment.setPaymentStatus(PaymentStatus.PENDING); 
	    	loan.setBalance(loan.getBalance() + needToPay); 
	    	repaymentRepository.save(repayment);
	    	loanApplicationRepository.save(loan);
	    	return; 
	    }


	    if(amountPaid >= minInterest)
	    {
	    	interestPaid = minInterest; 
	    	amountPaid -= minInterest; 
	    }
	    else
	    {
	    	interestPaid = amountPaid;
	    	needToPay += (minInterest - amountPaid);
	    	amountPaid = 0.0; 
	    }

	    if(amountPaid > 0)
	    {
	    	if(amountPaid >= minPrincipal)
	    	{
	    		principalPaid = minPrincipal; 
	    		loan.setBalance(loan.getBalance() - principalPaid); 
	    		amountPaid -= minPrincipal; 
	    		if (amountPaid > 0) { 
	    		    loan.setBalance(loan.getBalance() - amountPaid); 
	    		    System.out.println("Overpayment detected: " + amountPaid + " applied to loan balance.");
	    		}
	    	}
	    	else
	    	{
	    		principalPaid = amountPaid; 
	    		loan.setBalance(loan.getBalance() - principalPaid); 
	    		needToPay += (minPrincipal - amountPaid); 
	    		amountPaid = 0.0; 
	    	}
	    }

	    if (needToPay > 0) {
	        loan.setBalance(loan.getBalance() + needToPay);
	    }

	    repayment.setInterestPaid(interestPaid);
	    repayment.setPrincipalPaid(principalPaid);
	    repayment.setPaymentDate(paymentDate);

	    repayment.setPaymentStatus(PaymentStatus.COMPLETED);


	    repaymentRepository.save(repayment);
	    loanApplicationRepository.save(loan); 
	    if(loan.getBalance() <= 0.001) {
	    	System.out.println("Loan is Settled");
	    	loanApplicationRepository.save(loan); 
	    }

	    else {
	    	Repayment nextRepayment = new Repayment();
	    	nextRepayment.setLoanApplication(loan);
	    	nextRepayment.setDueDate(repayment.getDueDate().plusMonths(1));
	    	nextRepayment.setPaymentStatus(PaymentStatus.PENDING);

	    	double remainingLoan = loan.getBalance();
	    	
	    	int remainingTenure = (int) (loan.getLoanProduct().getTenure() - getMonthsBetween(loan.getApprovedDate(), nextRepayment.getDueDate()));

	    	if(remainingTenure <= 0)
	    	{
	    		System.out.println("No more Scheduled repayments, loan should be settled");
	    	}
	    	else
	    	{
	   
	    		double nextInterestAmount=(remainingLoan*(loan.getLoanProduct().getInterestRate()/100))/remainingTenure;
	    		double nextPrincipleAmount=remainingLoan/remainingTenure; 

	    		nextRepayment.setAmountDue(nextPrincipleAmount);
	    		nextRepayment.setInterestAmount(nextInterestAmount);
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
		repayment.setInterestAmount(calculateTotalInterest(repayment.getLoanApplication())/repayment.getLoanApplication().getLoanProduct().getTenure());
		repaymentRepository.save(repayment);
	}


	private double calculateTotalInterest(LoanApplication loanApplication) {
	    if (loanApplication.getLoanProduct() == null || loanApplication.getLoanProduct().getTenure() == null || loanApplication.getLoanProduct().getTenure() <= 0 || loanApplication.getLoanProduct().getInterestRate() == null) 
	    {
	        return 0.0;
	    }  
	    double balance = loanApplication.getBalance(); 
	    int tenure = loanApplication.getLoanProduct().getTenure();
	    double interestRate = loanApplication.getLoanProduct().getInterestRate();
	    return (balance * interestRate) / 100.0;
	}


	@Override
	public List<Repayment> getRepayementByLoanApplicationById(Long loanApplicationId) {
		LoanApplication loanApplication=loanApplicationRepository.findById(loanApplicationId).get();
		
		
		List<Repayment> repayments= repaymentRepository.findByLoanApplication(loanApplication);
		return repayments;
		
		// TODO Auto-generated method stub
		
	}
}
