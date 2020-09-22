package com.example.timeline;

public class DeleteReportInfo {
    private String date,email,fullName,phone,reason,uid,userName;

    public DeleteReportInfo() {
    }

    public DeleteReportInfo(String date, String email, String fullName, String phone, String reason, String uid, String userName) {
        this.date = date;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.reason = reason;
        this.uid = uid;
        this.userName = userName;
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

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }
}
