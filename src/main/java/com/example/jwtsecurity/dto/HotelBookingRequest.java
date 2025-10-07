package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class HotelBookingRequest {
    
    @NotNull(message = "Hotel ID is required")
    private Long hotelId;
    
    @NotNull(message = "Room ID is required")
    private Long roomId;
    
    @NotBlank(message = "Check-in date is required")
    private String checkInDate;
    
    @NotBlank(message = "Check-out date is required")
    private String checkOutDate;
    
    @NotNull(message = "Guest count is required")
    @Min(value = 1, message = "Guest count must be at least 1")
    @Max(value = 10, message = "Guest count cannot exceed 10")
    private Integer guestCount;
    
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal totalPrice;
    
    private String currency = "TRY";
    
    private String notes;
    
    public HotelBookingRequest() {}
    
    public HotelBookingRequest(Long hotelId, Long roomId, String checkInDate, String checkOutDate, 
                              Integer guestCount, BigDecimal totalPrice) {
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestCount = guestCount;
        this.totalPrice = totalPrice;
    }
    
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    
    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    
    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }
    
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    // gece sayısı
    public Integer getNights() {
        if (checkInDate != null && checkOutDate != null) {
            try {
                java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInDate);
                java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutDate);
                return (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            } catch (Exception e) {
                return 1; // varsayılan 1 gece
            }
        }
        return 1;
    }
}
