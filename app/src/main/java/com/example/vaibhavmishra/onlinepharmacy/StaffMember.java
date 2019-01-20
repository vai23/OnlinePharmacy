package com.example.vaibhavmishra.onlinepharmacy;

public class StaffMember {

    private String ownerUID;
    private String name;
    private String address;
    private int age;
    private String username;
    private String password;
    private String id;

    public StaffMember() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StaffMember(String ownerUID, String name, String address, int age, String username, String password) {
        this.ownerUID=ownerUID;
        this.name=name;
        this.address=address;
        this.age=age;
        this.username=username;
        this.password=password;
    }

    public String getownerUID() {
        return ownerUID;
    }

    public void setownerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setID(String id) {
        this.id=id;
    }

    public String getID() {
        return id;
    }
}
