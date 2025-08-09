package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.HotelRequest;
import com.example.jwtsecurity.dto.HotelResponse;
import com.example.jwtsecurity.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) { this.service = service; }

    // admin customer
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public List<HotelResponse> list() {
        return service.listAll();
    }

    // admin
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    } // otel ekleme


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public HotelResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    // admin
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponse update(@PathVariable Long id, @Valid @RequestBody HotelRequest req) {
        return service.update(id, req);
    }

    // admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String,String> delete(@PathVariable Long id) {
        service.delete(id);
        return Map.of("message", "Otel başarıyla silindi.");
    }
}
