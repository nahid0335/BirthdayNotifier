package com.example.birthdaynotifier;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "birthdate_table")
public class BirthDate {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String time;

    private String date;

    private Boolean notification;

    public BirthDate(String name, String time, String date, Boolean notification) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.notification = notification;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public Boolean getNotification() {
        return notification;
    }
}

