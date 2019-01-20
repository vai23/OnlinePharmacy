package com.example.vaibhavmishra.onlinepharmacy;

public class Medicine {

    private String ownerUID;
    private String name;
    private int code;
    private float cost;
    private String expiryDate;
    private String shelfNumber;
    private int quantity;
    private String id;

    public Medicine() {}

    public Medicine(String ownerUID, String name, int code, float cost, String expiryDate, String shelfNumber, int quantity) {
        this.ownerUID = ownerUID;
        this.name = name;
        this.code = code;
        this.cost = cost;
        this.expiryDate = expiryDate;
        this.shelfNumber = shelfNumber;
        this.quantity=quantity;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setID(String id) {
        this.id=id;
    }

    public String getID() {
        return id;
    }
}
