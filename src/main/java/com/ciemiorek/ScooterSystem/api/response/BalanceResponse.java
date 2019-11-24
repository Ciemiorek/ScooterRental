package com.ciemiorek.ScooterSystem.api.response;

import java.math.BigDecimal;

public class BalanceResponse extends BasicResponse {

    public BalanceResponse() {
    }

    private BigDecimal balance;

    public BalanceResponse(String responseMsg, BigDecimal balance) {
        super(responseMsg);
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
