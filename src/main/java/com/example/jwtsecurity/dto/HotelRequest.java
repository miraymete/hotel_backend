package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public class HotelRequest {
    @NotBlank private String name;
    @NotBlank private String city;
    @Min(1) @Max(5) private int stars;
    private String region;
    private String country;
    private BigDecimal pricePerNight;
    private String currency;
    private Double ratingScore;
    private String ratingLabel;
    private Integer reviewCount;
    private String imageUrl;
    private Set<String> amenities;
    private Boolean lastMinute;

    public HotelRequest() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

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

    public Set<String> getAmenities() { return amenities; }
    public void setAmenities(Set<String> amenities) { this.amenities = amenities; }

    public Boolean getLastMinute() { return lastMinute; }
    public void setLastMinute(Boolean lastMinute) { this.lastMinute = lastMinute; }
}
// otel ekleme ve güncelleme istekleri