package com.example.timeline;

public class ProjectInfo {
    private String id,name,category,description, framework,platform,requirement,prerequisite,status,startingDate,endDate,modifiedUser,
        modifiedDate,createdDate,createdUser,totalTasks,totalTaskPoints,completedTask,completedTaskPoints,modifiedVersion;

    public ProjectInfo(){}

    public ProjectInfo(String id, String name, String category, String description, String framework, String platform, String requirement,
                       String prerequisite, String status, String startingDate, String endDate, String modifiedUser, String modifiedDate,
                       String createdDate, String createdUser, String totalTasks, String totalTaskPoints, String completedTask,
                       String completedTaskPoints,String modifiedVersion) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.framework = framework;
        this.platform = platform;
        this.requirement = requirement;
        this.prerequisite = prerequisite;
        this.status = status;
        this.startingDate = startingDate;
        this.endDate = endDate;
        this.modifiedUser = modifiedUser;
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.totalTasks = totalTasks;
        this.totalTaskPoints = totalTaskPoints;
        this.completedTask = completedTask;
        this.completedTaskPoints = completedTaskPoints;
        this.modifiedVersion = modifiedVersion;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getFramework() {
        return framework;
    }

    public String getPlatform() {
        return platform;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public String getStatus() {
        return status;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public String getTotalTasks() {
        return totalTasks;
    }

    public String getTotalTaskPoints() {
        return totalTaskPoints;
    }

    public String getCompletedTask() {
        return completedTask;
    }

    public String getCompletedTaskPoints() {
        return completedTaskPoints;
    }

    public String getModifiedVersion() {
        return modifiedVersion;
    }
}
