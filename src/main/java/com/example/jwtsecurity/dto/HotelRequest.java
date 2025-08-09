package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.*;

public class HotelRequest {
    @NotBlank private String name;
    @NotBlank private String city;
    @Min(1) @Max(5) private int stars;

    public HotelRequest() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
}
// otel ekleme ve güncelleme istekleri