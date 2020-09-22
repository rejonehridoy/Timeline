package com.example.timeline;

import androidx.annotation.NonNull;

public class MemberInfo {
    private String id,fullName,userName,role;
    public MemberInfo(){}

    public MemberInfo(String id, String fullName, String userName, String role) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.role = role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    @NonNull
    @Override
    public String toString() {
        return getUserName();
    }
}
