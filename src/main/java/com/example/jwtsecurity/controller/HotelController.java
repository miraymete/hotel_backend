package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.HotelRequest;
import com.example.jwtsecurity.dto.HotelResponse;
import com.example.jwtsecurity.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }
    // son dakika soldaki kutu herkese açık
    @GetMapping("/last-minute")
    public Page<HotelResponse> lastMinute(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        var p = service.lastMinute(page, size);
        return p;
    }

    // herkese açık
    @GetMapping
    public List<HotelResponse> list() {
        return service.listAll();
    }

    // herkese açık
    @GetMapping("/{id}")
    public HotelResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    // admin
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    // admin
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponse update(@PathVariable Long id, @Valid @RequestBody HotelRequest req) {
        return service.update(id, req);
    }
    // arama herkese açık
    @GetMapping("/search")
    public Page<HotelResponse> search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) Integer minStars,
                                      @RequestParam(required = false) Integer maxStars,
                                      @RequestParam(required = false) BigDecimal minPrice,
                                      @RequestParam(required = false) BigDecimal maxPrice,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "12") int size) {
        return service.search(q, minStars, maxStars, minPrice, maxPrice, page, size);
    }

    // admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> delete(@PathVariable Long id) {
        service.delete(id);
        return Map.of("message", "Otel başarıyla silindi.");
    }
}
