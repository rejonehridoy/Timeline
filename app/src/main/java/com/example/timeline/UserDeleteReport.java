package com.example.timeline;

public class UserDeleteReport {
    String uid,userName,date,email,fullName,phone,reason;

    public UserDeleteReport(String uid,String userName, String date, String email, String fullName, String phone, String reason) {
        this.uid = uid;
        this.userName = userName;
        this.date = date;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.reason = reason;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getReason() {
        return reason;
    }
}
