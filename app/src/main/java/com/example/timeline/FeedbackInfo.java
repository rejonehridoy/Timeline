package com.example.timeline;

public class FeedbackInfo {
    private String id,appVersion,subject,message,userName,fullName,email,review,date,reply;

    public FeedbackInfo() {
    }

    public FeedbackInfo(String id, String appVersion, String subject, String message, String userName, String fullName, String email, String review, String date, String reply) {
        this.id = id;
        this.appVersion = appVersion;
        this.subject = subject;
        this.message = message;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.review = review;
        this.date = date;
        this.reply = reply;
    }

    public String getId() {
        return id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getReview() {
        return review;
    }

    public String getDate() {
        return date;
    }

    public String getReply() {
        return reply;
    }
}
