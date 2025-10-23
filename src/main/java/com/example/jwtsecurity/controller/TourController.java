package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.TourRequest;
import com.example.jwtsecurity.dto.TourResponse;
import com.example.jwtsecurity.service.TourService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// tur işlemleri için 
@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    // tüm turlar listesi herkese açık
    @GetMapping
    public List<TourResponse> list() {
        return tourService.listAll();
    }

    @GetMapping("/{id}")
    public TourResponse get(@PathVariable Long id) {
        return tourService.getById(id);
    }

    @GetMapping("/recommended")
    public List<TourResponse> getRecommended() {
        return tourService.getRecommended();
    }

    // kategoriye göre turlar herkese açık
    @GetMapping("/category/{category}")
    public Page<TourResponse> getByCategory(@PathVariable String category,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "12") int size) {
        return tourService.findByCategory(category, page, size);
    }

    // arama 
    @GetMapping("/search")
    public Page<TourResponse> search(@RequestParam(required = false) String q,
                                    @RequestParam(required = false) BigDecimal minPrice,
                                    @RequestParam(required = false) BigDecimal maxPrice,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "12") int size) {
        return tourService.search(q, minPrice, maxPrice, page, size);
    }

    // kategori fiyat aralığı
    @GetMapping("/filter")
    public Page<TourResponse> filter(@RequestParam(required = false) String category,
                                    @RequestParam(required = false) BigDecimal minPrice,
                                    @RequestParam(required = false) BigDecimal maxPrice,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "12") int size) {
        return tourService.findByCategoryAndPrice(category, minPrice, maxPrice, page, size);
    }

    // yeni tur oluşturma
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TourResponse> create(@Valid @RequestBody TourRequest request) {
        TourResponse response = tourService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // güncelleme
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TourResponse update(@PathVariable Long id, @Valid @RequestBody TourRequest request) {
        return tourService.update(id, request);
    }

    // sil
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> delete(@PathVariable Long id) {
        tourService.delete(id);
        return Map.of("message", "Tour başarıyla silindi.");
    }
}
