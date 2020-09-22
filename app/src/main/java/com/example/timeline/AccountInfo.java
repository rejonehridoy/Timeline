package com.example.timeline;

public class AccountInfo {


    private int id;
    private String userName;
    private String accountName;
    private String accountLogo;
    private String accountUserName;
    private String accountEmail;
    private String accountPhone;
    private String accountPriority;
    private String accountPassword;
    private String accountNotes;
    private String  accountCreatedDate;
    private String  accountModifiedDate;


    public AccountInfo(int id, String userName,String accountName, String accountLogo, String accountUserName, String accountEmail, String accountPhone,
                       String accountPriority, String accountPassword, String accountNotes, String accountCreatedDate,String accountModifiedDate) {
        this.id = id;
        this.userName = userName;
        this.accountName = accountName;
        this.accountLogo = accountLogo;
        this.accountUserName = accountUserName;
        this.accountEmail = accountEmail;
        this.accountPhone = accountPhone;
        this.accountPriority = accountPriority;
        this.accountPassword = accountPassword;
        this.accountNotes = accountNotes;
        this.accountCreatedDate = accountCreatedDate;
        this.accountModifiedDate = accountModifiedDate;
    }
    public AccountInfo(int id,String userName,String accountName,String accountLogo,String accountUserName,String accountEmail,String accountPhone,String accountPriority){
        this.id = id;
        this.userName = userName;
        this.accountName = accountName;
        this.accountLogo = accountLogo;
        this.accountUserName = accountUserName;
        this.accountEmail = accountEmail;
        this.accountPhone = accountPhone;
        this.accountPriority = accountPriority;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountLogo() {
        return accountLogo;
    }

    public String getAccountUserName() {
        return accountUserName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public String getAccountPriority() {
        return accountPriority;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public String getAccountNotes() {
        return accountNotes;
    }

    public String getAccountCreatedDate() {
        return accountCreatedDate;
    }
}
