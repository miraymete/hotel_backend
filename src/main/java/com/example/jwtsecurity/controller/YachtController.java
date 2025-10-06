package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.YachtRequest;
import com.example.jwtsecurity.dto.YachtResponse;
import com.example.jwtsecurity.service.YachtService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/yachts")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173", 
    "http://localhost:5174",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:5174",
    "https://hotel-frontend-ts-zsjq.vercel.app"
}, allowCredentials = "true")
public class YachtController {

    private final YachtService yachtService;

    public YachtController(YachtService yachtService) {
        this.yachtService = yachtService;
    }

    // Tüm yatları listele (herkese açık)
    @GetMapping
    public List<YachtResponse> list() {
        return yachtService.listAll();
    }

    // ID ile yacht getir (herkese açık)
    @GetMapping("/{id}")
    public YachtResponse get(@PathVariable Long id) {
        return yachtService.getById(id);
    }

    // Önerilen yatları getir (herkese açık)
    @GetMapping("/recommended")
    public List<YachtResponse> getRecommended() {
        return yachtService.getRecommended();
    }

    // Kategoriye göre yatları getir (herkese açık)
    @GetMapping("/category/{category}")
    public Page<YachtResponse> getByCategory(@PathVariable String category,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "12") int size) {
        return yachtService.findByCategory(category, page, size);
    }

    // Arama (herkese açık)
    @GetMapping("/search")
    public Page<YachtResponse> search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) BigDecimal minPrice,
                                      @RequestParam(required = false) BigDecimal maxPrice,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "12") int size) {
        return yachtService.search(q, minPrice, maxPrice, page, size);
    }

    // Kategori ve fiyat aralığına göre arama (herkese açık)
    @GetMapping("/filter")
    public Page<YachtResponse> filter(@RequestParam(required = false) String category,
                                      @RequestParam(required = false) BigDecimal minPrice,
                                      @RequestParam(required = false) BigDecimal maxPrice,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "12") int size) {
        return yachtService.findByCategoryAndPrice(category, minPrice, maxPrice, page, size);
    }

    // Yeni yacht oluştur (Admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<YachtResponse> create(@Valid @RequestBody YachtRequest request) {
        YachtResponse response = yachtService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Yacht güncelle (Admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public YachtResponse update(@PathVariable Long id, @Valid @RequestBody YachtRequest request) {
        return yachtService.update(id, request);
    }

    // Yacht sil (Admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> delete(@PathVariable Long id) {
        yachtService.delete(id);
        return Map.of("message", "Yacht başarıyla silindi.");
    }
}
