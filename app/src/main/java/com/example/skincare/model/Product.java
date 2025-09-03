package com.example.skincare.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Product {



    private int id;
    private String name;
    private String slug;
    private String description;

    @SerializedName("expected_results")
    private String expectedResults;

    @SerializedName("usage_instructions")
    private String usageInstructions;

    @SerializedName("time_of_use")
    private String timeOfUse;

    @SerializedName("shelf_life")
    private String shelfLife;

    @SerializedName("incompatible_products")
    private String incompatibleProducts;

    private String image;

    @SerializedName("recommended_for")
    private String recommendedFor;

    @SerializedName("recommended_by_doctors")
    private List<Doctor> recommendedByDoctors;

    private static final String BASE_IMAGE_URL = "http://192.168.100.47:8000/image/product/";

    // ✅ Empty constructor required for Gson
    public Product() {}

    // All-args constructor
    public Product(int id, String name, String slug, String description, String expectedResults,
                   String usageInstructions, String timeOfUse, String shelfLife,
                   String incompatibleProducts, String image, String recommendedFor,
                   List<Doctor> recommendedByDoctors) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.expectedResults = expectedResults;
        this.usageInstructions = usageInstructions;
        this.timeOfUse = timeOfUse;
        this.shelfLife = shelfLife;
        this.incompatibleProducts = incompatibleProducts;
        this.image = image;
        this.recommendedFor = recommendedFor;
        this.recommendedByDoctors = recommendedByDoctors;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
    public String getExpectedResults() { return expectedResults; }
    public String getUsageInstructions() { return usageInstructions; }
    public String getTimeOfUse() { return timeOfUse; }
    public String getShelfLife() { return shelfLife; }
    public String getIncompatibleProducts() { return incompatibleProducts; }

//     ✅ Always return full image URL
    public String getImage() {
        if (image != null && !image.startsWith("http")) {
            return BASE_IMAGE_URL + image;
        }
        return image;
    }

    public String getRecommendedFor() { return recommendedFor; }
    public List<Doctor> getRecommendedByDoctors() { return recommendedByDoctors; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setDescription(String description) { this.description = description; }
    public void setExpectedResults(String expectedResults) { this.expectedResults = expectedResults; }
    public void setUsageInstructions(String usageInstructions) { this.usageInstructions = usageInstructions; }
    public void setTimeOfUse(String timeOfUse) { this.timeOfUse = timeOfUse; }
    public void setShelfLife(String shelfLife) { this.shelfLife = shelfLife; }
    public void setIncompatibleProducts(String incompatibleProducts) { this.incompatibleProducts = incompatibleProducts; }
    public void setImage(String image) { this.image = image; }
    public void setRecommendedFor(String recommendedFor) { this.recommendedFor = recommendedFor; }
    public void setRecommendedByDoctors(List<Doctor> recommendedByDoctors) { this.recommendedByDoctors = recommendedByDoctors; }
}
