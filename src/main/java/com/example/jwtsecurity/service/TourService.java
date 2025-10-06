package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.TourRequest;
import com.example.jwtsecurity.dto.TourResponse;
import com.example.jwtsecurity.model.Tour;
import com.example.jwtsecurity.repository.TourRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // Tüm turları listele
    public List<TourResponse> listAll() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ID ile tour bul
    public TourResponse getById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));
        return convertToResponse(tour);
    }

    // Kategoriye göre turları bul
    public Page<TourResponse> findByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tour> tours = tourRepository.findByCategory(category, pageable);
        return tours.map(this::convertToResponse);
    }

    // Önerilen turları bul
    public List<TourResponse> getRecommended() {
        List<Tour> tours = tourRepository.findByIsRecommendedTrue();
        return tours.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Arama
    public Page<TourResponse> search(String query, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tour> tours = tourRepository.searchTours(query, minPrice, maxPrice, pageable);
        return tours.map(this::convertToResponse);
    }

    // Kategori ve fiyat aralığına göre arama
    public Page<TourResponse> findByCategoryAndPrice(String category, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tour> tours = tourRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
        return tours.map(this::convertToResponse);
    }

    // Yeni tour oluştur (Admin)
    public TourResponse create(TourRequest request) {
        Tour tour = new Tour();
        tour.setName(request.getName());
        tour.setDescription(request.getDescription());
        tour.setLocation(request.getLocation());
        tour.setCategory(request.getCategory());
        tour.setDuration(request.getDuration());
        tour.setGroupSize(request.getGroupSize());
        tour.setPrice(request.getPrice());
        tour.setCurrency(request.getCurrency());
        tour.setRatingScore(request.getRatingScore());
        tour.setRatingLabel(request.getRatingLabel());
        tour.setReviewCount(request.getReviewCount());
        tour.setImageUrl(request.getImageUrl());
        tour.setIsRecommended(request.getIsRecommended());
        tour.setTags(request.getTags());

        Tour savedTour = tourRepository.save(tour);
        return convertToResponse(savedTour);
    }

    // Tour güncelle (Admin)
    public TourResponse update(Long id, TourRequest request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));

        tour.setName(request.getName());
        tour.setDescription(request.getDescription());
        tour.setLocation(request.getLocation());
        tour.setCategory(request.getCategory());
        tour.setDuration(request.getDuration());
        tour.setGroupSize(request.getGroupSize());
        tour.setPrice(request.getPrice());
        tour.setCurrency(request.getCurrency());
        tour.setRatingScore(request.getRatingScore());
        tour.setRatingLabel(request.getRatingLabel());
        tour.setReviewCount(request.getReviewCount());
        tour.setImageUrl(request.getImageUrl());
        tour.setIsRecommended(request.getIsRecommended());
        tour.setTags(request.getTags());

        Tour updatedTour = tourRepository.save(tour);
        return convertToResponse(updatedTour);
    }

    // Tour sil (Admin)
    public void delete(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new RuntimeException("Tour not found with id: " + id);
        }
        tourRepository.deleteById(id);
    }

    // Tour entity'sini TourResponse'a dönüştür
    private TourResponse convertToResponse(Tour tour) {
        return new TourResponse(
                tour.getId(),
                tour.getName(),
                tour.getDescription(),
                tour.getLocation(),
                tour.getCategory(),
                tour.getDuration(),
                tour.getGroupSize(),
                tour.getPrice(),
                tour.getCurrency(),
                tour.getRatingScore(),
                tour.getRatingLabel(),
                tour.getReviewCount(),
                tour.getImageUrl(),
                tour.getIsRecommended(),
                tour.getTags()
        );
    }
}
