package com.example.jwtsecurity.dto;

import java.math.BigDecimal;
import java.util.Set;

public class HotelResponse {
    private Long id;
    private String name;
    private String city;
    private int stars;
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
    public HotelResponse() {}
    public HotelResponse(Long id, String name, String city, int stars) {
        this.id = id; this.name = name; this.city = city; this.stars = stars;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public int getStars() { return stars; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
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
// otel bilgileri