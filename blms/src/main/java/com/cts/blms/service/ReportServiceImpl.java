package com.cts.blms.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.blms.entity.Customer;
import com.cts.blms.entity.LoanApplication;
import com.cts.blms.entity.LoanProduct;
import com.cts.blms.entity.Repayment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	LoanApplicationService loanApplicationService;

	@Autowired
	RepaymentService repaymentService;

	@Autowired
	CustomerService customerService;

	@Override
	public byte[] generateRepaymentReport(Long id) {
		// TODO Auto-generated method stub
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(id);
		LoanProduct loanProduct = loanApplication.getLoanProduct();

		Table repaymentSchedule = new Table(UnitValue.createPercentArray(new float[] { 1, 2, 2, 2, 2, 2 }))
				.setWidth(UnitValue.createPercentValue(100));

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf, PageSize.A4);
			document.setMargins(20, 20, 20, 20);

			Paragraph title = new Paragraph("REPAYMENT REPORT").setBold().setFontSize(20);

			title.setTextAlignment(TextAlignment.CENTER);
			document.add(title);
			double balance = loanApplication.getBalance() > 0 ? loanApplication.getBalance() : 0;
			document.add(new Paragraph("LOAN DETAILS:").setBold().setFontSize(16))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY);
			document.add(new Paragraph("Loan Application ID : " + loanApplication.getLoanApplicationId()));
			document.add(new Paragraph("Loan Type : " + loanProduct.getProductName()));
			document.add(new Paragraph("Tenure : " + loanProduct.getTenure()));
			document.add(new Paragraph("Requested Amount : " + loanApplication.getRequestAmount()));
			document.add(new Paragraph("Interest Rate : " + loanProduct.getInterestRate()));
			document.add(new Paragraph("Applied On : " + loanApplication.getApplicationDate()));
			document.add(new Paragraph("Approved On : " + loanApplication.getApprovedDate()));
			document.add(new Paragraph("Outstanding Balance : " + balance));
			Paragraph fineNote = new Paragraph("Note: A fine of ₹10 per day will be charged for delayed payments.")
					.setFontSize(12).setItalic().setFontColor(ColorConstants.RED).setMarginTop(20);
			document.add(fineNote);

			String[] headers = { "No", "Due Date", "Principal amount", "Interest Amount", "Total amount per month",
					"Accumulated Total Payment" };
			for (String header : headers) {
				repaymentSchedule.addHeaderCell(
						new Cell().add(new Paragraph(header)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold()
								.setTextAlignment(TextAlignment.CENTER));
			}

			double totalAmount = loanApplication.getRequestAmount();
			int tenure = loanProduct.getTenure(); // in months
			double principal = totalAmount / tenure;
			double interestRate = loanProduct.getInterestRate(); // assuming this is annual
			double monthlyInterest = (totalAmount * (interestRate / 100)) / 12;
			double accumulatedAmount = 0;

			LocalDate dueDate = loanApplication.getApprovedDate().plusMonths(1);
			for (int i = 0; i < tenure; i++) {
				accumulatedAmount += principal + monthlyInterest;
				repaymentSchedule.addCell(new Paragraph(String.valueOf(i + 1)));
				repaymentSchedule.addCell(new Paragraph(dueDate.toString()));
				repaymentSchedule.addCell(new Paragraph(String.format("%.2f", principal)));
				repaymentSchedule.addCell(new Paragraph(String.format("%.2f", monthlyInterest)));
				repaymentSchedule.addCell(new Paragraph(String.format("%.2f", principal + monthlyInterest)));
				repaymentSchedule.addCell(new Paragraph(String.format("%.2f", accumulatedAmount)));
				dueDate = dueDate.plusMonths(1); // Corrected: update dueDate
			}

			document.add(repaymentSchedule);
			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generating PDF report", e);
		}
	}

	public byte[] documentOutStandingLoanReport(List<LoanApplication> loanApplications) {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf, PageSize.A4);
			document.setMargins(20, 20, 20, 20);

			for (int i = 0; i < loanApplications.size(); i++) {
				LoanApplication loanApplication = loanApplications.get(i);
				LoanProduct loanProduct = loanApplication.getLoanProduct();

				// Title
				Paragraph title = new Paragraph("LOAN REPORT").setBold().setFontSize(20)
						.setTextAlignment(TextAlignment.CENTER);
				document.add(title);
				Table customerDetails = getCustomerIntable(loanApplication.getCustomer());
				document.add(customerDetails);
				// Loan Details
				double balance = Math.max(loanApplication.getBalance(), 0);
				document.add(new Paragraph("LOAN DETAILS:").setBold().setFontSize(16)
						.setBackgroundColor(ColorConstants.LIGHT_GRAY));
				document.add(new Paragraph("Loan Application ID : " + loanApplication.getLoanApplicationId()));
				document.add(new Paragraph("Loan Type : " + loanProduct.getProductName()));
				document.add(new Paragraph("Tenure : " + loanProduct.getTenure()));
				document.add(new Paragraph("Requested Amount : " + loanApplication.getRequestAmount()));
				document.add(new Paragraph("Interest Rate : " + loanProduct.getInterestRate()));
				document.add(new Paragraph("Applied On : " + loanApplication.getApplicationDate()));
				document.add(new Paragraph("Approved On : " + loanApplication.getApprovedDate()));
				document.add(new Paragraph("Outstanding Balance : " + balance));

				// Fine Note
				Paragraph fineNote = new Paragraph("Note: A fine of ₹10 per day will be charged for delayed payments.")
						.setFontSize(12).setItalic().setFontColor(ColorConstants.RED).setMarginTop(20);
				document.add(fineNote);

				// Payment Schedule Table
				document.add(new Paragraph("PAYMENT SCHEDULE :").setBold().setFontColor(ColorConstants.GRAY)
						.setTextAlignment(TextAlignment.LEFT).setMargin(0).setPadding(5));
				Table table = paymentScheduleTable(loanApplication);
				document.add(table);

				// Add page break if not the last loan
				if (i < loanApplications.size() - 1) {
					document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
				}
			}

			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generating PDF report", e);
		}
	}

	private Table getCustomerIntable(Customer customer) {
		// TODO Auto-generated method stub

		Table customerDetails = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 2, 1, 2 }))
				.useAllAvailableWidth();

		customerDetails.addHeaderCell(new Cell(1, 9).add(new Paragraph("CUSTOMER DETAILS"))
				.setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER));

		customerDetails.addCell(new Paragraph(String.valueOf(customer.getCustomerId())));
		customerDetails.addCell(new Paragraph(customer.getName()));
		customerDetails.addCell(new Paragraph(customer.getEmail()));
		customerDetails.addCell(new Paragraph(customer.getPhone()));
		customerDetails.addCell(new Paragraph(customer.getAddress()));
		return customerDetails;

	}

	public byte[] generateLoanReport(Long id) {
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(id);
		LoanProduct loanProduct = loanApplication.getLoanProduct();
		repaymentService.getRepayementByLoanApplicationById(id);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf, PageSize.A4);
			document.setMargins(20, 20, 20, 20);

			Paragraph title = new Paragraph("LOAN REPORT").setBold().setFontSize(20);
			title.setTextAlignment(TextAlignment.CENTER);
			document.add(title);
			double balance = loanApplication.getBalance() > 0 ? loanApplication.getBalance() : 0;
			document.add(new Paragraph("LOAN DETAILS:").setBold().setFontSize(16))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY);
			document.add(new Paragraph("Loan Application ID : " + loanApplication.getLoanApplicationId()));
			document.add(new Paragraph("Loan Type : " + loanProduct.getProductName()));
			document.add(new Paragraph("Tenure : " + loanProduct.getTenure()));
			document.add(new Paragraph("Requested Amount : " + loanApplication.getRequestAmount()));
			document.add(new Paragraph("Interest Rate : " + loanProduct.getInterestRate()));
			document.add(new Paragraph("Applied On : " + loanApplication.getApplicationDate()));
			document.add(new Paragraph("Approved On : " + loanApplication.getApprovedDate()));
			document.add(new Paragraph("Outstanding Balance : " + balance));
//            document.flush();
//
//           pdf.addNewPage(PageSize.A4.rotate());
//           document.setMargins(20, 20, 20, 20); // Reset margins for new layout
//           document = new Document(pdf, PageSize.A4.rotate());

			document.add(new Paragraph("PAYMENT SCHEDULE :").setBold().setFontColor(ColorConstants.GRAY)
					.setTextAlignment(TextAlignment.LEFT).setMargin(0).setPadding(5));
			Table table = paymentScheduleTable(loanApplication);
			document.add(table);
//         
			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generating PDF report", e);
		}
	}

	private Table paymentScheduleTable(LoanApplication loanApplication) {
		// TODO Auto-generated method stub

		Table paymentSchedule = new Table(UnitValue.createPercentArray(new float[] { 1, 2, 2, 2, 2, 2, 2, 2, 2 }))
				.useAllAvailableWidth();

		List<Repayment> repayments = repaymentService
				.getRepayementByLoanApplicationById(loanApplication.getLoanApplicationId());
		String[] headers = { "No", "Principal Paid", "Interest Paid", "Loan Repayment", "Due Date", "Payment Date",
				"Fine Amount", "Accumulated Total Payment", "Payment Status" };
		for (String header : headers) {
			paymentSchedule.addHeaderCell(new Cell().add(new Paragraph(header))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold().setTextAlignment(TextAlignment.CENTER));
		}

		List<Repayment> paidRepayments = repayments.stream().sorted(Comparator.comparing(Repayment::getDueDate))
				.toList();
		int i = 0;
		double accumulated = 0;
		if (paidRepayments.size() > 0) {
			for (Repayment r : paidRepayments) {
				accumulated += r.getAmountDue() + r.getInterestAmount();
				paymentSchedule.addCell(new Cell().add(new Paragraph("" + (i + 1))));
				paymentSchedule.addCell(new Cell().add(new Paragraph(
						"" + String.format("%.2f", r.getPrincipalPaid() != null ? r.getPrincipalPaid() : 0))));
				paymentSchedule.addCell(new Cell().add(new Paragraph(
						"" + String.format("%.2f", (r.getInterestPaid() != null ? r.getInterestPaid() : 0)))));
				paymentSchedule.addCell(new Cell().add(new Paragraph(
						"" + String.format("%.2f", (r.getPrincipalPaid() != null ? r.getPrincipalPaid() : 0)
								+ (r.getInterestPaid() != null ? r.getInterestPaid() : 0)))));
				paymentSchedule
						.addCell(new Cell().add(new Paragraph("" + (r.getDueDate() != null ? r.getDueDate() : "-"))));
				paymentSchedule.addCell(
						new Cell().add(new Paragraph("" + (r.getPaymentDate() != null ? r.getPaymentDate() : "-"))));
				paymentSchedule.addCell(new Cell().add(new Paragraph("" + r.getFineAmount())));
				paymentSchedule.addCell(new Cell().add(new Paragraph("" + String.format("%.2f", accumulated))));
				paymentSchedule.addCell(new Cell().add(new Paragraph("" + r.getPaymentStatus())));
				i++;
			}
		}
		return paymentSchedule;

	}

	@Override
	public byte[] generateCustomerOutStandingLoanReport(Long id) {
		// TODO Auto-generated method stub
		Customer customer = customerService.getCustomerDetailsById(id);
		List<LoanApplication> loanApplications = loanApplicationService.getCustomerOutstandingLoans(customer);
		return documentOutStandingLoanReport(loanApplications);
	}

	@Override
	public byte[] generateOutStandingLoanReport() {
		// TODO Auto-generated method stub
		List<LoanApplication> loanApplications = loanApplicationService.getAllOutstandingLoans(); // Fetch all
																									// outstanding loans
		return documentOutStandingLoanReport(loanApplications);
	}

	@Override
	public byte[] getPortfolio(Long id) {

		Customer customer = customerService.getCustomerDetailsById(id);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf, PageSize.A4);
			Paragraph title = new Paragraph("CUSTOMER PORTFOLIO").setBold().setFontSize(18)
					.setTextAlignment(TextAlignment.CENTER).setMarginBottom(10);
			document.add(title);
			if (customer.getProfileImage() != null) {
				ImageData imageData = ImageDataFactory.create(customer.getProfileImage());

				Image profileImage = new Image(imageData).scaleToFit(100, 100);
				profileImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
				document.add(profileImage);
			}

			Table table = new Table(UnitValue.createPercentArray(new float[] { 2, 5 })).useAllAvailableWidth();
			table.addCell(new Cell().add(new Paragraph("Customer ID").setBold()));
			table.addCell(new Cell().add(new Paragraph(String.valueOf(customer.getCustomerId()))));
			table.addCell(new Cell().add(new Paragraph("Name").setBold()));
			table.addCell(new Cell().add(new Paragraph(customer.getName())));
			table.addCell(new Cell().add(new Paragraph("Email").setBold()));
			table.addCell(new Cell().add(new Paragraph(customer.getEmail())));
			table.addCell(new Cell().add(new Paragraph("Phone").setBold()));
			table.addCell(new Cell().add(new Paragraph(customer.getPhone())));
			table.addCell(new Cell().add(new Paragraph("Address").setBold()));
			table.addCell(new Cell().add(new Paragraph(customer.getAddress())));

			document.add(table);
			document.add(new Paragraph("Loan History : ").setBold().setFontSize(16));
			List<LoanApplication> loans = loanApplicationService.getLoanApplicationByCustomer(customer);
			Table loanDetails = new Table(UnitValue.createPercentArray(new float[] { 1, 2, 2, 3, 3, 2, 1, 2 }))
					.useAllAvailableWidth();
			String[] headers = { "LOAN ID", "LOAN TYPE", "REQUESTED AMOUNT", "APPLIED ON", "APPROVED ON",
					"INTEREST RATE", "TENURE", "BALANCE" };
			for (String header : headers) {
				loanDetails.addHeaderCell(
						new Cell().add(new Paragraph(header)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setBold()
								.setTextAlignment(TextAlignment.CENTER));
			}

			for (LoanApplication loan : loans) {
				loanDetails.addCell(new Cell().add(new Paragraph(String.valueOf(loan.getLoanApplicationId()))));
				loanDetails.addCell(new Cell().add(new Paragraph(loan.getLoanProduct().getProductName())));
				loanDetails.addCell(new Cell().add(new Paragraph(String.format("%.2f", loan.getRequestAmount()))));
				loanDetails.addCell(new Cell().add(new Paragraph(String.valueOf(loan.getApplicationDate()))));
				loanDetails.addCell(new Cell().add(new Paragraph(String.valueOf(loan.getApprovedDate()))));
				loanDetails.addCell(new Cell()
						.add(new Paragraph(String.format("%.2f%%", loan.getLoanProduct().getInterestRate()))));
				loanDetails.addCell(new Cell().add(new Paragraph(String.valueOf(loan.getLoanProduct().getTenure()))));
				loanDetails.addCell(new Cell().add(new Paragraph(String.format("%.2f", loan.getBalance()))));
			}

			document.add(loanDetails);
			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generating PDF report", e);
		}
	}
}
