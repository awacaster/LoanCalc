package com.example.loancalculator;

// Model class for loan calculations
public class LoanCalculator {
    // Instance variables to store loan parameters
    private double principal;        // Loan amount
    private double annualRate;       // Annual interest rate in percentage
    private int years;               // Loan term in years
    private int paymentsPerYear = 12; // Default to monthly

    // Constructor with default values
    public LoanCalculator() {
        principal = 0;
        annualRate = 0;
        years = 1;
    }

    // Setters for loan parameters
    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public void setAnnualRate(double annualRate) {
        this.annualRate = annualRate;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public void setPaymentsPerYear(int paymentsPerYear) {
        this.paymentsPerYear = paymentsPerYear;
    }

    // Calculate payment per period using the formula:
    // PMT = P * r * (1 + r)^n / ((1 + r)^n - 1)
    // where: P = principal
    //        r = period interest rate (annual rate / payments per year)
    //        n = total number of payments (years * payments per year)
    public double calculateMonthlyPayment() {
        double periodRate = (annualRate / 100) / paymentsPerYear;
        int totalPayments = years * paymentsPerYear;
        if (periodRate == 0) {
            return principal / totalPayments;
        }
        return (principal * periodRate * Math.pow(1 + periodRate, totalPayments))
                / (Math.pow(1 + periodRate, totalPayments) - 1);
    }

    // Calculate total payment over the life of the loan
    // Total Payment = Payment per period * Total Number of Payments
    public double calculateTotalPayment() {
        int totalPayments = years * paymentsPerYear;
        return calculateMonthlyPayment() * totalPayments;
    }
}