package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class YachtBookingRequest {
    
    @NotNull(message = "Yacht ID is required")
    private Long yachtId;
    
    @NotBlank(message = "Booking date is required")
    private String bookingDate;
    
    @NotBlank(message = "Start time is required")
    private String startTime;
    
    @NotNull(message = "Guest count is required")
    @Min(value = 1, message = "Guest count must be at least 1")
    @Max(value = 20, message = "Guest count cannot exceed 20")
    private Integer guestCount;
    
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal totalPrice;
    
    private String currency = "TRY";
    
    private String notes;
    
    public YachtBookingRequest() {}
    
    public YachtBookingRequest(Long yachtId, String bookingDate, String startTime, 
                              Integer guestCount, BigDecimal totalPrice) {
        this.yachtId = yachtId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.guestCount = guestCount;
        this.totalPrice = totalPrice;
    }
    
    public Long getYachtId() { return yachtId; }
    public void setYachtId(Long yachtId) { this.yachtId = yachtId; }
    
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getDepartureDate() {
        return bookingDate;
    }
    
    // varsayılan 4 saat 
    public Integer getDurationHours() {
        return 4;
    }
}
