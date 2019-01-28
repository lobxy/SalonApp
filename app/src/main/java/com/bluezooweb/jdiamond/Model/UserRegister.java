package com.bluezooweb.jdiamond.Model;

public class UserRegister {

    public UserRegister() {
    }

    String device_token, name, uid, gender, contact;

    public UserRegister(String device_token, String name, String uid, String gender, String contact) {
        this.device_token = device_token;
        this.name = name;
        this.uid = uid;
        this.gender = gender;
        this.contact = contact;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
