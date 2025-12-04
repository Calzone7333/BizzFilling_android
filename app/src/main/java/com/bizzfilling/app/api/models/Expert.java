package com.bizzfilling.app.api.models;

import java.util.List;

public class Expert {
    private String id;
    private String name;
    private String qualification;
    private String experience;
    private Double rating;
    private Integer reviews;
    private List<String> languages;
    private List<String> specialization;
    private String price;
    private Boolean available;
    private String bio;
    private String image; // Base64 or ID, usually handled as string

    public String getId() { return id; }
    public String getName() { return name; }
    public String getQualification() { return qualification; }
    public String getExperience() { return experience; }
    public Double getRating() { return rating; }
    public Integer getReviews() { return reviews; }
    public List<String> getLanguages() { return languages; }
    public List<String> getSpecialization() { return specialization; }
    public String getPrice() { return price; }
    public Boolean getAvailable() { return available; }
    public String getBio() { return bio; }
    public String getImage() { return image; }
}
