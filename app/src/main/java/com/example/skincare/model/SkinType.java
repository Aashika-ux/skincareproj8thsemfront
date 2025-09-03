package com.example.skincare.model;

import com.google.gson.annotations.SerializedName;

public class SkinType {

    private int id;
    private String name;
    private String description;

    @SerializedName("image_url")
    private String imageUrl;  // use camelCase for getter

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }  // <--- this fixes your adapter

    // Setters (optional)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
