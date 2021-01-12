package com.example.finalproject;

public class Challange {


    public String uid, tag, category, photo, description, title,usefull_links;

    public String getUsefull_links() {
        return usefull_links;
    }

    public void setUsefull_links(String usefull_links) {
        this.usefull_links = usefull_links;
    }

    public Challange()
    {

    }

    public  Challange(String uid, String tag, String category, String photo, String description, String title, String usefull_links) {
        this.uid = uid;
        this.tag = tag;
        this.category = category;
        this.photo = photo;
        this.description = description;
        this.title = title;
        this.usefull_links=usefull_links;

    }
    public String getUid() {
        return uid;
    }

    public String getTag() {
        return tag;
    }

    public String getCategory() {
        return category;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

}
