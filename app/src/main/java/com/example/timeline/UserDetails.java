package com.example.timeline;

public class UserDetails {
    private String uid,userName,fullName,email,password,phone,gender,dateOfBirth,avaterCode,status,userType,createdDate,recoveryQuestion,
        recoveryAns,recoveryDate,lastVisit;

    public UserDetails(){

    }
    public UserDetails(String uid, String userName, String fullName, String email, String password, String phone, String gender,
                       String dateOfBirth, String avaterCode, String status, String userType, String createdDate, String recoveryQuestion,
                       String recoveryAns, String recoveryDate, String lastVisit) {
        this.uid = uid;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avaterCode = avaterCode;
        this.status = status;
        this.userType = userType;
        this.createdDate = createdDate;
        this.recoveryQuestion = recoveryQuestion;
        this.recoveryAns = recoveryAns;
        this.recoveryDate = recoveryDate;
        this.lastVisit = lastVisit;
    }

    public String getUid() {
        return uid;
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

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAvaterCode() {
        return avaterCode;
    }

    public String getStatus() {
        return status;
    }

    public String getUserType() {
        return userType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getRecoveryQuestion() {
        return recoveryQuestion;
    }

    public String getRecoveryAns() {
        return recoveryAns;
    }

    public String getRecoveryDate() {
        return recoveryDate;
    }

    public String getLastVisit() {
        return lastVisit;
    }
}
