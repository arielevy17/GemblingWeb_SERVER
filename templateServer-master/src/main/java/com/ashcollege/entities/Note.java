package com.ashcollege.entities;

import java.util.Date;

public class Note {
    private int id;
    private String content;
    private Date date;
    private User owner;

    public Note(int id, String content, Date date, User owner) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.owner = owner;
    }

    public Note() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
