package com.example.tailorz.CustomerModels;

public class NotificationModel {
    String heading, description, date, time, userName;

    public NotificationModel() {
    }

    public NotificationModel(String heading, String description, String date, String time, String userName) {
        this.heading = heading;
        this.description = description;
        this.date = date;
        this.time = time;
        this.userName = userName;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
