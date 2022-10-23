package com.example.tabapplication.model;

public class Categories {

    String Title;
    String categoriesID;

    public Categories() {
    }

    public Categories(String Title, String categoriesID) {
        this.Title = Title;
        this.categoriesID = categoriesID;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setCategoriesID(String categoriesID) {
        this.categoriesID = categoriesID;
    }

    public String getTitle() {
        return Title;
    }

    public String getCategoriesID() {
        return categoriesID;
    }
}
