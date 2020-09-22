package com.example.timeline;

public class TaskInfo {
    private String pid,tid,name,serial,category,description,solution,deadline,assigner,points,issues,link,status,priority,modifiedDate,
            modifiedUser,createdDate,createdUser,comments;

    public TaskInfo(){}

    public TaskInfo(String pid, String tid, String name, String serial, String category, String description, String solution,
                    String deadline, String assigner, String points, String issues, String link, String status, String priority,
                    String modifiedDate, String modifiedUser, String createdDate, String createdUser, String comments) {
        this.pid = pid;
        this.tid = tid;
        this.name = name;
        this.serial = serial;
        this.category = category;
        this.description = description;
        this.solution = solution;
        this.deadline = deadline;
        this.assigner = assigner;
        this.points = points;
        this.issues = issues;
        this.link = link;
        this.status = status;
        this.priority = priority;
        this.modifiedDate = modifiedDate;
        this.modifiedUser = modifiedUser;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.comments = comments;
    }

    public String getPid() {
        return pid;
    }

    public String getTid() {
        return tid;
    }

    public String getName() {
        return name;
    }

    public String getSerial() {
        return serial;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getSolution() {
        return solution;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getAssigner() {
        return assigner;
    }

    public String getPoints() {
        return points;
    }

    public String getIssues() {
        return issues;
    }

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public String getComments() {
        return comments;
    }
}
