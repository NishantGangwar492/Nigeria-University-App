package com.iotait.schoolapp.ui.homepage.ui.leaderboard.model;

public class userDetailsModel {
    String personname;
    String photo;
    String phone;
    String email;

    public userDetailsModel() {
    }

    public userDetailsModel(String personname, String photo, String phone, String email) {
        this.personname = personname;
        this.photo = photo;
        this.phone = phone;
        this.email = email;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
