package com.example.jwtsecurity.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String city;
    @Column(nullable = false) private int stars; // 1..5
    // ---- EKLENEN ALANLAR ----
    @Column private String region;        // ilçe/bölge
    @Column private String country;       // ülke

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerNight;     // gecelik fiyat

    @Column private String currency;      // TL, EUR...
    @Column private Double ratingScore;   // 0..10
    @Column private String ratingLabel;   // "Fevkalade"
    @Column private Integer reviewCount;  // 3124 gibi

    @Column private String imageUrl;      // kart görseli
    @Column private Boolean lastMinute = false; // son dakika etiketi

    // basit özellik listesi (WiFi, Spa, vb.)
    @ElementCollection
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity")
    private Set<String> amenities = new HashSet<>();


    public Hotel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

    public Boolean getLastMinute() { return lastMinute; }
    public void setLastMinute(Boolean lastMinute) { this.lastMinute = lastMinute; }

    public Set<String> getAmenities() { return amenities; }
    public void setAmenities(Set<String> amenities) { this.amenities = amenities; }
}
