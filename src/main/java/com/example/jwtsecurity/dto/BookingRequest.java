package com.example.jwtsecurity.dto;

import com.example.jwtsecurity.model.Booking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {
    
    @NotNull(message = "Booking type is required")
    private Booking.BookingType bookingType;

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Check-in date is required")
    private String checkInDate;

    private String checkOutDate; // Tours için gerekli olmayabilir

    @NotBlank(message = "Booking date is required")
    private String bookingDate;

    @Positive(message = "Guest count must be positive")
    private Integer guestCount = 1;

    private String notes;

    // Constructors
    public BookingRequest() {}

    public BookingRequest(Booking.BookingType bookingType, Long itemId, String itemName,
                         String checkInDate, String bookingDate, Integer guestCount) {
        this.bookingType = bookingType;
        this.itemId = itemId;
        this.itemName = itemName;
        this.checkInDate = checkInDate;
        this.bookingDate = bookingDate;
        this.guestCount = guestCount;
    }

    // Getters and Setters
    public Booking.BookingType getBookingType() { return bookingType; }
    public void setBookingType(Booking.BookingType bookingType) { this.bookingType = bookingType; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
