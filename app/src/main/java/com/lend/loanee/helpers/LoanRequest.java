package com.lend.loanee.helpers;

public class LoanRequest {

    double amount;
    String interest;
    String period;
    String status;
    String fullname;
    String app_ID="";

    public LoanRequest(double amount, String interest, String period, String status, String fullname, String app_ID) {
        this.amount = amount;
        this.interest = interest;
        this.period = period;
        this.status = status;
        this.fullname = fullname;
        this.app_ID = app_ID;
    }

    public String getApp_ID() {
        return app_ID;
    }

    public double getAmount() {
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
