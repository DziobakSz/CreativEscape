package com.example.finalproject.ui.home;

public class Challange {


    public String uid, tag, category, photo, description, title,usefull_links, video_address;
    boolean  of_the_day;

    public String getUsefull_links() {
        return usefull_links;
    }

    public void setUsefull_links(String usefull_links) {
        this.usefull_links = usefull_links;
    }

    public String getVideo_address() {
        return video_address;
    }

    public void setVideo_address(String video_address) {
        this.video_address = video_address;
    }

    public Challange()
    {

    }

    public boolean isOf_the_day() {
        return of_the_day;
    }

    public void setOf_the_day(boolean of_the_day) {
        this.of_the_day = of_the_day;
    }

    public  Challange(String uid, String tag, String category, String photo, String description, String title, String usefull_links, String video_address, boolean of_the_day) {
        this.uid = uid;
        this.of_the_day=of_the_day;
        this.video_address=video_address;
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
