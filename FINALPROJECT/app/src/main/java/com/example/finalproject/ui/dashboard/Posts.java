package com.example.finalproject.ui.dashboard;

public class Posts {
    public String uid;
    public String date;
    public String time;
    public String username;
    public String description;
    public String post_image;
    public String profile_image;
    public String tag;
    public int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Posts(){

    }
    public Posts(String uid, String tag, String date, String time, String username, String description, String post_image, String profile_image,int counter) {
        this.uid = uid;
        this.date = date;
        this.time = time;
        this.username = username;
        this.description = description;
        this.post_image = post_image;
        this.profile_image = profile_image;
        this.tag = tag;
        this.counter=counter;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
