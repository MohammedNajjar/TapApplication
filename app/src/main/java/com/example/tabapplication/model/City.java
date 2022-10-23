package com.example.tabapplication.model;

public class City {
    private int id;
    private String id_str;
    private String name;
    private double deliveryPrice;

    public City() {
    }

    public City(int id, String id_str, String name, double deliveryPrice) {
        this.id = id;
        this.id_str = id_str;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
