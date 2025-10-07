package com.example.jwtsecurity.service;

import com.example.jwtsecurity.model.Yacht;
import com.example.jwtsecurity.repository.YachtRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class YachtService {

    private final YachtRepository yachtRepository;

    public YachtService(YachtRepository yachtRepository) {
        this.yachtRepository = yachtRepository;
    }

    public List<Yacht> getAllActiveYachts() {
        return yachtRepository.findByIsActiveTrue();
    }

    public Optional<Yacht> getYachtById(Long id) {
        return yachtRepository.findById(id);
    }

    public List<Yacht> getYachtsByType(String type) {
        return yachtRepository.findByTypeAndIsActiveTrue(type);
    }

    public List<Yacht> getYachtsByLocation(String location) {
        return yachtRepository.findByLocationContainingIgnoreCaseAndIsActiveTrue(location);
    }

    public List<Yacht> getYachtsByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return yachtRepository.findByBasePriceBetweenAndIsActiveTrue(minPrice, maxPrice);
    }

    public Yacht createYacht(Yacht yacht) {
        return yachtRepository.save(yacht);
    }

    public Yacht updateYacht(Long id, Yacht yacht) {
        Yacht existingYacht = yachtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yacht not found with id: " + id));
        
        existingYacht.setName(yacht.getName());
        existingYacht.setDescription(yacht.getDescription());
        existingYacht.setLocation(yacht.getLocation());
        existingYacht.setType(yacht.getType());
        existingYacht.setYachtLength(yacht.getYachtLength());
        existingYacht.setMaxCapacity(yacht.getMaxCapacity());
        existingYacht.setDurationHours(yacht.getDurationHours());
        existingYacht.setBasePrice(yacht.getBasePrice());
        existingYacht.setCurrency(yacht.getCurrency());
        existingYacht.setRating(yacht.getRating());
        existingYacht.setReviewCount(yacht.getReviewCount());
        existingYacht.setImageUrls(yacht.getImageUrls());
        existingYacht.setAmenities(yacht.getAmenities());
        existingYacht.setCaptainIncluded(yacht.getCaptainIncluded());
        existingYacht.setFuelIncluded(yacht.getFuelIncluded());
        
        return yachtRepository.save(existingYacht);
    }

    // sil
    public void deleteYacht(Long id) {
        Yacht yacht = yachtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yacht not found with id: " + id));
        
        yacht.setIsActive(false);
        yachtRepository.save(yacht);
    }

    public List<Yacht> searchYachts(String query, String type, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        if (query != null && !query.trim().isEmpty()) {
            return yachtRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(query);
        }
        
        if (type != null && !type.trim().isEmpty()) {
            return getYachtsByType(type);
        }
        
        if (minPrice != null && maxPrice != null) {
            return getYachtsByPriceRange(minPrice, maxPrice);
        }
        
        return getAllActiveYachts();
    }

    public List<String> getDistinctTypes() {
        return yachtRepository.findDistinctTypes();

    }

    public List<String> getDistinctLocations() {
        return yachtRepository.findDistinctLocations();
    }
}