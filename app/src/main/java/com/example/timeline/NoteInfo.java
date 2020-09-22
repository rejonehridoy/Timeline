package com.example.timeline;

public class NoteInfo {
    private int id;
    private String userName,subject,description,date,time,dayOfWeek,modifiedDate,favorite,backup;

    public NoteInfo(int id, String userName, String subject, String description, String date, String time, String dayOfWeek, String modifiedDate, String favorite,String backup) {
        this.id = id;
        this.userName = userName;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.modifiedDate = modifiedDate;
        this.favorite = favorite;
        this.backup = backup;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getBackup() {
        return backup;
    }
}
