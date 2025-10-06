package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public class TourRequest {
    
    @NotBlank(message = "Tour name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotBlank(message = "Group size is required")
    private String groupSize;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String currency = "TRY";

    private Double ratingScore;
    private String ratingLabel;
    private Integer reviewCount;
    private String imageUrl;
    private Boolean isRecommended = false;
    private Set<String> tags;

    // Constructors
    public TourRequest() {}

    public TourRequest(String name, String description, String location, String category,
                      String duration, String groupSize, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.duration = duration;
        this.groupSize = groupSize;
        this.price = price;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getGroupSize() { return groupSize; }
    public void setGroupSize(String groupSize) { this.groupSize = groupSize; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getRatingScore() { return ratingScore; }
    public void setRatingScore(Double ratingScore) { this.ratingScore = ratingScore; }

    public String getRatingLabel() { return ratingLabel; }
    public void setRatingLabel(String ratingLabel) { this.ratingLabel = ratingLabel; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Boolean isRecommended) { this.isRecommended = isRecommended; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
}
