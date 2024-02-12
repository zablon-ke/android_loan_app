package com.lend.loanee.helpers;

public class LoanRequest {

    String amount;
    String interest;
    String period;
    String status;
    String fullname;

    public LoanRequest(String amount, String interest, String period, String status, String fullname) {
        this.amount = amount;
        this.interest = interest;
        this.period = period;
        this.status = status;
        this.fullname = fullname;
    }

    public String getAmount() {
        return amount;
    }

    public String getInterest() {
        return interest;
    }

    public String getPeriod() {
        return period;
    }

    public String getStatus() {
        return status;
    }

    public String getFullname() {
        return fullname;
    }
}
