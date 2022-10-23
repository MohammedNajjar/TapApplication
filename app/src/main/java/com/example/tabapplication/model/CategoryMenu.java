package com.example.tabapplication.model;

public class CategoryMenu {
    private int id ;
    private String cat_id ;
    private String name ;
    private String image;

    public CategoryMenu() {
    }

    public CategoryMenu(int id, String cat_id, String name, String image) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
