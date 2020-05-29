package com.example.birthdaynotifier;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "birthdate_table")
public class BirthDate {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int day;
    private int month;
    private Boolean notification;

    public BirthDate(String name, int day,int month, Boolean notification) {
        this.name = name;
        this.day = day;
        this.month = month;
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

    public int getDay() {
        return day;
    }

    public int getMonth() { return month; }

    public Boolean getNotification() {
        return notification;
    }
}

