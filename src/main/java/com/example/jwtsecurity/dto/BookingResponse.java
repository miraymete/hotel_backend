package com.example.jwtsecurity.dto;

import com.example.jwtsecurity.model.Booking;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private Long userId;
    private String username;
    private Booking.BookingType bookingType;
    private Long itemId;
    private String itemName;
    private String checkInDate;
    private String checkOutDate;
    private String bookingDate;
    private Integer guestCount;
    private BigDecimal totalPrice;
    private String currency;
    private Booking.BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public BookingResponse() {}

    public BookingResponse(Long id, Long userId, String username, Booking.BookingType bookingType,
                          Long itemId, String itemName, String checkInDate, String checkOutDate,
                          String bookingDate, Integer guestCount, BigDecimal totalPrice,
                          String currency, Booking.BookingStatus status, String notes,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.bookingType = bookingType;
        this.itemId = itemId;
        this.itemName = itemName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = bookingDate;
        this.guestCount = guestCount;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

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

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Booking.BookingStatus getStatus() { return status; }
    public void setStatus(Booking.BookingStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
