package com.example.tabapplication.model;

public class Item {
    private int id;
    private String title;
    private int price;
    private String image;
    private String describe;
    private String catg_id;

    public Item() {
    }

    public Item(int id, String title, int price, String image, String describe,String catg_id) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.describe = describe;
        this.catg_id = catg_id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }



    public String getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(String catg_id) {
        this.catg_id = catg_id;
    }
}
