package com.example.timeline;

public class NoticeInfo {
    private String id;
    private String visible,subject,category,description,eventDate,eventTime,tag,creator,createdDate,repetition,modifer,modifiedDate,dayOfWeek,link;

    public NoticeInfo(){

    }
    public NoticeInfo(String id, String subject, String category, String description, String eventDate, String eventTime, String tag, String creator, String createdDate, String repetition, String dayOfWeek, String link) {
        this.id = id;
        this.subject = subject;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.tag = tag;
        this.creator = creator;
        this.createdDate = createdDate;
        this.repetition = repetition;
        this.dayOfWeek = dayOfWeek;
        this.link = link;
    }

    public NoticeInfo(String id, String visible, String subject, String category, String description, String eventDate, String eventTime, String tag, String creator, String createdDate, String repetition, String modifer, String modifiedDate, String dayOfWeek, String link) {
        this.id = id;
        this.visible = visible;
        this.subject = subject;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.tag = tag;
        this.creator = creator;
        this.createdDate = createdDate;
        this.repetition = repetition;
        this.modifer = modifer;
        this.modifiedDate = modifiedDate;
        this.dayOfWeek = dayOfWeek;
        this.link = link;
    }

    public String getVisible() {
        return visible;
    }

    public String getModifer() {
        return modifer;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getTag() {
        return tag;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getRepetition() {
        return repetition;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getLink() {
        return link;
    }
}
