package com.example.jwtsecurity.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "yachts")
public class Yacht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String category; // Lüks, Klasik, Katamaran, Tekne

    @Column(nullable = false)
    private String capacity; // 12 kişi, 14 kişi, vb.

    @Column(nullable = false)
    private String length; // 24m, 21m, vb.

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private String currency = "TRY"; // TRY, USD, EUR

    @Column
    private Double ratingScore; // 0..10

    @Column
    private String ratingLabel; // "Mükemmel", "Çok İyi"

    @Column
    private Integer reviewCount;

    @Column
    private String imageUrl;

    @Column
    private Boolean isRecommended = false;

    // Yacht etiketleri (Lüks, Flybridge, vb.)
    @ElementCollection
    @CollectionTable(name = "yacht_tags", joinColumns = @JoinColumn(name = "yacht_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    public Yacht() {}

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

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }

    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

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
