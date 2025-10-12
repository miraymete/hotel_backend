package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.HotelBookingRequest;
import com.example.jwtsecurity.model.Hotel;
import com.example.jwtsecurity.model.Room;
import com.example.jwtsecurity.service.HotelService;
import com.example.jwtsecurity.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

//TODO: Bir admin panel tasarla

// otel işlemleri için
@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173",
    "http://localhost:5174",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:5174",
    "https://hotel-frontend-ts-zsjq.vercel.app"
}, allowCredentials = "true")
public class HotelController {

    private final HotelService hotelService;
    private final BookingService bookingService;

    public HotelController(HotelService hotelService, BookingService bookingService) {
        this.hotelService = hotelService;
        this.bookingService = bookingService;
    }

    // tüm aktif oteller
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllActiveHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<Room>> getHotelRooms(@PathVariable Long id) {
        List<Room> rooms = hotelService.getHotelRooms(id);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchHotels(@RequestParam(required = false) String query,
                                                   @RequestParam(required = false) String city,
                                                   @RequestParam(required = false) String country,
                                                   @RequestParam(required = false) java.math.BigDecimal minPrice,
                                                   @RequestParam(required = false) java.math.BigDecimal maxPrice) {
        List<Hotel> hotels = hotelService.searchHotels(query, city, country, minPrice, maxPrice);
        return ResponseEntity.ok(hotels);
    }

    //şehirler
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        List<String> cities = hotelService.getDistinctCities();
        return ResponseEntity.ok(cities);
    }

    //ülkeler
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {
        List<String> countries = hotelService.getDistinctCountries();
        return ResponseEntity.ok(countries);
    }

    @PostMapping("/{id}/book")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Object> bookHotel(@PathVariable Long id, 
                                          @Valid @RequestBody HotelBookingRequest request) {
        try {
            var booking = bookingService.createHotelBooking(request, id);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        Hotel newHotel = hotelService.createHotel(hotel);
        return ResponseEntity.ok(newHotel);
    }

    // otel güncelle admin
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @Valid @RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(id, hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    // otel sil admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}