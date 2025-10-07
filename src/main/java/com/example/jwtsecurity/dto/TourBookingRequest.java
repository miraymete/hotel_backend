package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TourBookingRequest {
    
    @NotNull(message = "Tour ID is required")
    private Long tourId;
    
    @NotBlank(message = "Tour date is required")
    private String tourDate;
    
    @NotNull(message = "Participant count is required")
    @Min(value = 1, message = "Participant count must be at least 1")
    @Max(value = 20, message = "Participant count cannot exceed 20")
    private Integer participantCount;
    
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal totalPrice;
    
    private String currency = "TRY";
    
    private String notes;
    
    public TourBookingRequest() {}
    
    public TourBookingRequest(Long tourId, String tourDate, Integer participantCount, BigDecimal totalPrice) {
        this.tourId = tourId;
        this.tourDate = tourDate;
        this.participantCount = participantCount;
        this.totalPrice = totalPrice;
    }
    
    public Long getTourId() { return tourId; }
    public void setTourId(Long tourId) { this.tourId = tourId; }
    
    public String getTourDate() { return tourDate; }
    public void setTourDate(String tourDate) { this.tourDate = tourDate; }
    
    public Integer getParticipantCount() { return participantCount; }
    public void setParticipantCount(Integer participantCount) { this.participantCount = participantCount; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
