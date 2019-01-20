package com.example.vaibhavmishra.onlinepharmacy;

public class Customer {

    private String name;
    private String doctorName;
    private String email;
    private String mobileNumber;
    private String ownerUID;
    private String id;

    public Customer(String name, String doctorName, String email, String mobileNumber) {
        this.name = name;
        this.doctorName = doctorName;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public Customer() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
