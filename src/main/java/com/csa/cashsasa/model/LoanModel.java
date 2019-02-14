package com.csa.cashsasa.model;


import android.content.Context;

public class LoanModel
{
    private String amount_borrowed, amount_payable, balance, date_borrowed, loan_int, insurance_int;
    private int status;
    Context ctx;

    public LoanModel(String amount_borrowed, String amount_payable, String balance, String date_borrowed, int status, String loan_int, String insurance_int)
    {
       this.amount_borrowed = amount_borrowed;
       this.amount_payable = amount_payable;
       this.balance = balance;
       this.date_borrowed = date_borrowed;
       this.status = status;
       this.loan_int = loan_int;
       this.insurance_int = insurance_int;
    }

    public void setAmount_borrowed(String amount_borrowed) {
        this.amount_borrowed = amount_borrowed;
    }

    public void setAmount_payable(String amount_payable) {
        this.amount_payable = amount_payable;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setDate_borrowed(String date_borrowed) {
        this.date_borrowed = date_borrowed;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLoan_int(String loan_int) {
        this.loan_int = loan_int;
    }

    public void setInsurance_int(String insurance_int) {
        this.insurance_int = insurance_int;
    }

    public String getAmount_borrowed() {
        return amount_borrowed;
    }

    public String getAmount_payable() {
        return amount_payable;
    }

    public String getBalance() {
        return balance;
    }

    public String getDate_borrowed() {
        return date_borrowed;
    }

    public int getStatus() {
        return status;
    }

    public String getLoan_int() {
        return loan_int;
    }

    public String getInsurance_int() {
        return insurance_int;
    }
}
