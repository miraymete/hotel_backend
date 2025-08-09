package com.example.jwtsecurity.dto;

public class HotelResponse {
    private Long id;
    private String name;
    private String city;
    private int stars;

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
}
// otel bilgileri