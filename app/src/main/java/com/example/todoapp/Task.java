package com.example.todoapp;

import java.io.Serializable;

class Task implements Serializable {
    public static final long serialUid = 20190511;

    private int id;
    private final String name;
    private final String date;
    private final String description;

    public Task(int id, String name, String date, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }
}
