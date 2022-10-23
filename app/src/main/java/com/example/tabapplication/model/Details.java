package com.example.tabapplication.model;

import androidx.fragment.app.Fragment;

public class Details {

    String Category; //Tab Name
    String nameOf;
    String ImageUrl;
    Fragment fragment;

    public Details() {
    }

    public Details(String category, String nameOf, String imageUrl, Fragment fragment) {
        Category = category;
        this.nameOf = nameOf;
        ImageUrl = imageUrl;
        this.fragment = fragment;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getNameOf() {
        return nameOf;
    }

    public void setNameOf(String nameOf) {
        this.nameOf = nameOf;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}