package com.example.birthdaynotifier;

public class BirthDateSQL {


    private long id;
    private String name;
    private int day;
    private int month;
    private int notification;


    public BirthDateSQL (){}

    public BirthDateSQL(String name, int day,int month, int notification) {
        this.name = name;
        this.day = day;
        this.month = month;
        this.notification = notification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() { return month; }

    public int getNotification() {
        return notification;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }
}
