package com.example.tabapplication.model;


import java.util.ArrayList;

public class Order {
    private double lat;
    private double lng;
    private String id;
    private int entity_id;
    private ArrayList<OrderItem> orderItems;
    private String status;
    private String statusWIFI;
    private String statusPARKING;
    private String name_owner_order;
    private String number;
    private String address;
    private double totalAmount;
    private ArrayList<OtherOrder> otherOrders;
    private String numOfChairs;
    String email;




    public Order() {
    }

    public Order(double lat, double lng, String id, int entity_id, ArrayList<OrderItem> orderItems, String status, String statusWIFI, String statusPARKING, String name_owner_order, String number, String address, double totalAmount, ArrayList<OtherOrder> otherOrders, String numOfChairs,String email) {
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.entity_id = entity_id;
        this.orderItems = orderItems;
        this.status = status;
        this.statusWIFI = statusWIFI;
        this.statusPARKING = statusPARKING;
        this.name_owner_order = name_owner_order;
        this.number = number;
        this.address = address;
        this.totalAmount = totalAmount;
        this.otherOrders = otherOrders;
        this.numOfChairs = numOfChairs;
        this.email=email;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumOfChairs() {
        return numOfChairs;
    }

    public void setNumOfChairs(String numOfChairs) {
        this.numOfChairs = numOfChairs;
    }

    public String getStatusWIFI() {
        return statusWIFI;
    }

    public void setStatusWIFI(String statusWIFI) {
        this.statusWIFI = statusWIFI;
    }

    public String getStatusPARKING() {
        return statusPARKING;
    }

    public void setStatusPARKING(String statusPARKING) {
        this.statusPARKING = statusPARKING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName_owner_order() {
        return name_owner_order;
    }

    public void setName_owner_order(String name_owner_order) {
        this.name_owner_order = name_owner_order;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<OtherOrder> getOtherOrders() {
        return otherOrders;
    }

    public void setOtherOrders(ArrayList<OtherOrder> otherOrders) {
        this.otherOrders = otherOrders;
    }
}
