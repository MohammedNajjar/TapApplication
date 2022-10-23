package com.example.tabapplication.model;

public class User {

    private int id;
    private String title;
    private String email;
    private String password;
    private String address;
    private String  phone;
    private String url_logo;
    private double lat;
    private double lng;

    public User() {
    }

    public User(int id, String title, String email, String password, String address, String phone, String url_logo, double lat, double lng) {
        this.id = id;
        this.title = title;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.url_logo = url_logo;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl_logo() {
        return url_logo;
    }

    public void setUrl_logo(String url_logo) {
        this.url_logo = url_logo;
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
}
