package com.example.jwtsecurity.dto;

import java.math.BigDecimal;
import java.util.Set;

public class TourResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String category;
    private String duration;
    private String groupSize;
    private BigDecimal price;
    private String currency;
    private Double ratingScore;
    private String ratingLabel;
    private Integer reviewCount;
    private String imageUrl;
    private Boolean isRecommended;
    private Set<String> tags;

    // Constructors
    public TourResponse() {}

    public TourResponse(Long id, String name, String description, String location, String category,
                       String duration, String groupSize, BigDecimal price, String currency,
                       Double ratingScore, String ratingLabel, Integer reviewCount,
                       String imageUrl, Boolean isRecommended, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.duration = duration;
        this.groupSize = groupSize;
        this.price = price;
        this.currency = currency;
        this.ratingScore = ratingScore;
        this.ratingLabel = ratingLabel;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.isRecommended = isRecommended;
        this.tags = tags;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
