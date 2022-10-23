package com.example.tabapplication.model;

public class OtherOrder {
    private int id;
    private String id_str;
    private String name;
    private String name_details;
    private String address;
    private String address_details;
    private String note;
    private double lat,log;

    public OtherOrder() {
    }

    public OtherOrder(int id, String id_str, String name, String name_details, String address, String address_details, String note, double lat, double log) {
        this.id = id;
        this.id_str = id_str;
        this.name = name;
        this.name_details = name_details;
        this.address = address;
        this.address_details = address_details;
        this.note = note;
        this.lat = lat;
        this.log = log;
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

    public String getName_details() {
        return name_details;
    }

    public void setName_details(String name_details) {
        this.name_details = name_details;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_details() {
        return address_details;
    }

    public void setAddress_details(String address_details) {
        this.address_details = address_details;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }
}
