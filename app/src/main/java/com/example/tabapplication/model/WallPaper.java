package com.example.tabapplication.model;

import java.util.ArrayList;

public class WallPaper {

    String Category;
    String ImageUrl;
    ArrayList<String> Tags;

    public WallPaper() {

    }

    public WallPaper(String category, String ImageUrl, ArrayList<String> tags) {
        Category = category;
        this.ImageUrl = ImageUrl;
        Tags = tags;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageURL) {
        ImageUrl = imageURL;
    }

    public ArrayList<String> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<String> tags) {
        Tags = tags;
    }
}
