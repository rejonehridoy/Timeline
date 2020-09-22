package com.example.timeline;

public class ActivityInfo {
    private int id;
    private String userName, activityType, name, category, place, price, duration, year, yourTeam, opponentTeam, language, review, notes,
            status, date, time, result, modifiedDate, backup;

    public ActivityInfo(int id, String userName, String activityType, String name, String category, String place, String price,
                        String duration, String year, String yourTeam, String opponentTeam, String language, String review, String notes,
                        String status, String date, String time, String result, String modifiedDate, String backup) {
        this.id = id;
        this.userName = userName;
        this.activityType = activityType;
        this.name = name;
        this.category = category;
        this.place = place;
        this.price = price;
        this.duration = duration;
        this.year = year;
        this.yourTeam = yourTeam;
        this.opponentTeam = opponentTeam;
        this.language = language;
        this.review = review;
        this.notes = notes;
        this.status = status;
        this.date = date;
        this.time = time;
        this.result = result;
        this.modifiedDate = modifiedDate;
        this.backup = backup;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPlace() {
        return place;
    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public String getYear() {
        return year;
    }

    public String getYourTeam() {
        return yourTeam;
    }

    public String getOpponentTeam() {
        return opponentTeam;
    }

    public String getLanguage() {
        return language;
    }

    public String getReview() {
        return review;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getResult() {
        return result;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getBackup() {
        return backup;
    }
}
