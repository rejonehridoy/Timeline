package com.example.timeline;

import androidx.annotation.NonNull;

public class MembersProject {
    String pid,mid,projectName,memberFullName,memberUserName,uid,role;

    public MembersProject(){}

    public MembersProject(String pid, String mid, String projectName, String memberFullName, String memberUserName, String uid, String role) {
        this.pid = pid;
        this.mid = mid;
        this.projectName = projectName;
        this.memberFullName = memberFullName;
        this.memberUserName = memberUserName;
        this.uid = uid;
        this.role = role;
    }

    public String getPid() {
        return pid;
    }

    public String getMid() {
        return mid;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getMemberFullName() {
        return memberFullName;
    }

    public String getMemberUserName() {
        return memberUserName;
    }

    public String getUid() {
        return uid;
    }

    public String getRole() {
        return role;
    }

    @NonNull
    @Override
    public String toString() {
        return getMemberUserName();

    }
}
