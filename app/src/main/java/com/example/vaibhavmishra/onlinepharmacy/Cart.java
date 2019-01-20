package com.example.vaibhavmishra.onlinepharmacy;

public class Cart {

    private String name;
    private String cost;
    private String quantity;

    public Cart(String name, String cost, String quantity) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalCost() {
        return Float.toString(Float.parseFloat(cost)*Integer.parseInt(quantity));
    }
}
