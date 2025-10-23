package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.model.Yacht;
import com.example.jwtsecurity.service.YachtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/yachts")
public class YachtController {

    private final YachtService yachtService;

    public YachtController(YachtService yachtService) {
        this.yachtService = yachtService;
    }

    // yatları sırala
    @GetMapping
    public List<Yacht> list() {
        return yachtService.getAllActiveYachts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Yacht> get(@PathVariable Long id) {
        return yachtService.getYachtById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public List<Yacht> getByType(@PathVariable String type) {
        return yachtService.getYachtsByType(type);
    }

    @GetMapping("/location/{location}")
    public List<Yacht> getByLocation(@PathVariable String location) {
        return yachtService.getYachtsByLocation(location);
    }

    @GetMapping("/price-range")
    public List<Yacht> getByPriceRange(@RequestParam BigDecimal minPrice,
                                       @RequestParam BigDecimal maxPrice) {
        return yachtService.getYachtsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/search")
    public List<Yacht> search(@RequestParam(required = false) String query,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) BigDecimal minPrice,
                              @RequestParam(required = false) BigDecimal maxPrice) {
        return yachtService.searchYachts(query, type, minPrice, maxPrice);
    }

    @GetMapping("/types")
    public List<String> getTypes() {
        return yachtService.getDistinctTypes();
    }

    @GetMapping("/locations")
    public List<String> getLocations() {
        return yachtService.getDistinctLocations();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Yacht> create(@RequestBody Yacht yacht) {
        Yacht createdYacht = yachtService.createYacht(yacht);
        return ResponseEntity.status(201).body(createdYacht);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Yacht> update(@PathVariable Long id, @RequestBody Yacht yacht) {
        try {
            Yacht updatedYacht = yachtService.updateYacht(id, yacht);
            return ResponseEntity.ok(updatedYacht);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            yachtService.deleteYacht(id);
            return ResponseEntity.ok(Map.of("message", "Yacht başarıyla silindi."));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}