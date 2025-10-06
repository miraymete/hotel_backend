package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.YachtRequest;
import com.example.jwtsecurity.dto.YachtResponse;
import com.example.jwtsecurity.model.Yacht;
import com.example.jwtsecurity.repository.YachtRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class YachtService {

    private final YachtRepository yachtRepository;

    public YachtService(YachtRepository yachtRepository) {
        this.yachtRepository = yachtRepository;
    }

    // Tüm yatları listele
    public List<YachtResponse> listAll() {
        List<Yacht> yachts = yachtRepository.findAll();
        return yachts.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ID ile yacht bul
    public YachtResponse getById(Long id) {
        Yacht yacht = yachtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yacht not found with id: " + id));
        return convertToResponse(yacht);
    }

    // Kategoriye göre yatları bul
    public Page<YachtResponse> findByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Yacht> yachts = yachtRepository.findByCategory(category, pageable);
        return yachts.map(this::convertToResponse);
    }

    // Önerilen yatları bul
    public List<YachtResponse> getRecommended() {
        List<Yacht> yachts = yachtRepository.findByIsRecommendedTrue();
        return yachts.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Arama
    public Page<YachtResponse> search(String query, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Yacht> yachts = yachtRepository.searchYachts(query, minPrice, maxPrice, pageable);
        return yachts.map(this::convertToResponse);
    }

    // Kategori ve fiyat aralığına göre arama
    public Page<YachtResponse> findByCategoryAndPrice(String category, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Yacht> yachts = yachtRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
        return yachts.map(this::convertToResponse);
    }

    // Yeni yacht oluştur (Admin)
    public YachtResponse create(YachtRequest request) {
        Yacht yacht = new Yacht();
        yacht.setName(request.getName());
        yacht.setDescription(request.getDescription());
        yacht.setLocation(request.getLocation());
        yacht.setCategory(request.getCategory());
        yacht.setCapacity(request.getCapacity());
        yacht.setLength(request.getLength());
        yacht.setPrice(request.getPrice());
        yacht.setCurrency(request.getCurrency());
        yacht.setRatingScore(request.getRatingScore());
        yacht.setRatingLabel(request.getRatingLabel());
        yacht.setReviewCount(request.getReviewCount());
        yacht.setImageUrl(request.getImageUrl());
        yacht.setIsRecommended(request.getIsRecommended());
        yacht.setTags(request.getTags());

        Yacht savedYacht = yachtRepository.save(yacht);
        return convertToResponse(savedYacht);
    }

    // Yacht güncelle (Admin)
    public YachtResponse update(Long id, YachtRequest request) {
        Yacht yacht = yachtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yacht not found with id: " + id));

        yacht.setName(request.getName());
        yacht.setDescription(request.getDescription());
        yacht.setLocation(request.getLocation());
        yacht.setCategory(request.getCategory());
        yacht.setCapacity(request.getCapacity());
        yacht.setLength(request.getLength());
        yacht.setPrice(request.getPrice());
        yacht.setCurrency(request.getCurrency());
        yacht.setRatingScore(request.getRatingScore());
        yacht.setRatingLabel(request.getRatingLabel());
        yacht.setReviewCount(request.getReviewCount());
        yacht.setImageUrl(request.getImageUrl());
        yacht.setIsRecommended(request.getIsRecommended());
        yacht.setTags(request.getTags());

        Yacht updatedYacht = yachtRepository.save(yacht);
        return convertToResponse(updatedYacht);
    }

    // Yacht sil (Admin)
    public void delete(Long id) {
        if (!yachtRepository.existsById(id)) {
            throw new RuntimeException("Yacht not found with id: " + id);
        }
        yachtRepository.deleteById(id);
    }

    // Yacht entity'sini YachtResponse'a dönüştür
    private YachtResponse convertToResponse(Yacht yacht) {
        return new YachtResponse(
                yacht.getId(),
                yacht.getName(),
                yacht.getDescription(),
                yacht.getLocation(),
                yacht.getCategory(),
                yacht.getCapacity(),
                yacht.getLength(),
                yacht.getPrice(),
                yacht.getCurrency(),
                yacht.getRatingScore(),
                yacht.getRatingLabel(),
                yacht.getReviewCount(),
                yacht.getImageUrl(),
                yacht.getIsRecommended(),
                yacht.getTags()
        );
    }
}
