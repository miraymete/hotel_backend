package com.example.jwtsecurity.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tours")
public class Tour {
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
    private String category; // Doğa, Macera, Şehir, Tarih

    @Column(nullable = false)
    private String duration; // 3 saat, 5 saat, vb.

    @Column(nullable = false)
    private String groupSize; // 12 kişi, 15 kişi, vb.

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

    // Tour etiketleri (Doğa, Macera, vb.)
    @ElementCollection
    @CollectionTable(name = "tour_tags", joinColumns = @JoinColumn(name = "tour_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    public Tour() {}

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
