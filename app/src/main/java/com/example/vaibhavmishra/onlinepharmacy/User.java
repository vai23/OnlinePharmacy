package com.example.vaibhavmishra.onlinepharmacy;

import java.util.ArrayList;

public class User {
    private String uid;
    private String username;
    private String email;
    private String shopname;
    private String ownername;

    public User() {
    }

    public User(String username, String email) {
        this.username=username;
        this.email=email;
    }

    public User(String username, String email, String ownername, String shopname) {
        this.username=username;
        this.email=email;
        this.ownername=ownername;
        this.shopname=shopname;

    }

    public User(String uid,String username, String email, String ownername, String shopname) {
        this.uid=uid;
        this.username=username;
        this.email=email;
        this.ownername=ownername;
        this.shopname=shopname;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
