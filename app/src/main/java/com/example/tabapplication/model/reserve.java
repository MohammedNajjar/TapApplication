package com.example.tabapplication.model;

public class reserve {
    private String image;
    private String categories_id,name;
    private String category;

    public reserve() {
    }

    public reserve(String image, String categories_id, String name, String category) {
        this.image = image;
        this.categories_id = categories_id;
        this.name = name;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(String categories_id) {
        this.categories_id = categories_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
