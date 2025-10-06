package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.BookingRequest;
import com.example.jwtsecurity.dto.BookingResponse;
import com.example.jwtsecurity.model.Booking;
import com.example.jwtsecurity.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173", 
    "http://localhost:5174",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:5174",
    "https://hotel-frontend-ts-zsjq.vercel.app"
}, allowCredentials = "true")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Yeni rezervasyon oluştur (Kullanıcı)
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Kullanıcının rezervasyonlarını getir
    @GetMapping("/my-bookings")
    public Page<BookingResponse> getMyBookings(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return bookingService.getUserBookings(page, size);
    }

    // Rezervasyonu iptal et (Kullanıcı)
    @PutMapping("/{id}/cancel")
    public BookingResponse cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    // Tüm rezervasyonları getir (Admin)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookingResponse> getAllBookings(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBookings(page, size);
    }

    // Rezervasyon durumunu güncelle (Admin)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponse updateBookingStatus(@PathVariable Long id, 
                                              @RequestBody Map<String, String> statusRequest) {
        String statusStr = statusRequest.get("status");
        Booking.BookingStatus status = Booking.BookingStatus.valueOf(statusStr.toUpperCase());
        return bookingService.updateBookingStatus(id, status);
    }
}
