package com.example.timeline;

import androidx.annotation.NonNull;

public class AccountDetails {
    private String accName;
    private int accLogo;

    public AccountDetails(String accName, int accLogo) {
        this.accName = accName;
        this.accLogo = accLogo;
    }

    public String getAccName() {
        return accName;
    }

    public int getAccLogo() {
        return accLogo;
    }

    @NonNull
    @Override
    public String toString() {
        return accName;
    }
}
