package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.HotelRequest;
import com.example.jwtsecurity.dto.HotelResponse;
import com.example.jwtsecurity.model.Hotel;
import com.example.jwtsecurity.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;


import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class HotelService {

    private final HotelRepository repo;

    public HotelService(HotelRepository repo) { this.repo = repo; }

    private HotelResponse toResponse(Hotel h) {
        HotelResponse r = new HotelResponse(h.getId(), h.getName(), h.getCity(), h.getStars());
        r.setRegion(h.getRegion());
        r.setCountry(h.getCountry());
        r.setPricePerNight(h.getPricePerNight());
        r.setCurrency(h.getCurrency());
        r.setRatingScore(h.getRatingScore());
        r.setRatingLabel(h.getRatingLabel());
        r.setReviewCount(h.getReviewCount());
        r.setImageUrl(h.getImageUrl());
        r.setAmenities(h.getAmenities());
        r.setLastMinute(h.getLastMinute());
        return r;
        // otel bilgilerini yanıt olarak döndürme
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> listAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    } // otelleri listeleme

    public Page<HotelResponse> lastMinute(int page, int size) {
        return repo.findByLastMinuteTrue(PageRequest.of(page, size))
                .map(this::toResponse);
    }

    public Page<HotelResponse> search(String q,
                                      Integer minStars, Integer maxStars,
                                      BigDecimal minPrice, BigDecimal maxPrice,
                                      int page, int size) {
        return repo.search(q, minStars, maxStars, minPrice, maxPrice, PageRequest.of(page, size))
                .map(this::toResponse);
    }
     // findByLastMinuteTrue() ve search() çağrıları
    public HotelResponse create(HotelRequest req) {
        Hotel h = new Hotel();
        h.setName(req.getName());
        h.setCity(req.getCity());
        h.setStars(req.getStars());
        h.setRegion(req.getRegion());
        h.setCountry(req.getCountry());
        h.setPricePerNight(req.getPricePerNight());
        h.setCurrency(req.getCurrency());
        h.setRatingScore(req.getRatingScore());
        h.setRatingLabel(req.getRatingLabel());
        h.setReviewCount(req.getReviewCount());
        h.setImageUrl(req.getImageUrl());
        if (req.getAmenities() != null) h.setAmenities(req.getAmenities());
        h.setLastMinute(Boolean.TRUE.equals(req.getLastMinute()));
        return toResponse(repo.save(h));
    } // otel ekleme

    @Transactional(readOnly = true)
    public HotelResponse getById(Long id) {
        Hotel h = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Otel bulunamadı."));
        return toResponse(h);
    } // otel bilgilerini ID ile alma

    public HotelResponse update(Long id, HotelRequest req) {
        Hotel h = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Otel bulunamadı."));
        h.setName(req.getName());
        h.setCity(req.getCity());
        h.setStars(req.getStars());
        h.setRegion(req.getRegion());
        h.setCountry(req.getCountry());
        h.setPricePerNight(req.getPricePerNight());
        h.setCurrency(req.getCurrency());
        h.setRatingScore(req.getRatingScore());
        h.setRatingLabel(req.getRatingLabel());
        h.setReviewCount(req.getReviewCount());
        h.setImageUrl(req.getImageUrl());
        if (req.getAmenities() != null) h.setAmenities(req.getAmenities());
        h.setLastMinute(Boolean.TRUE.equals(req.getLastMinute()));
        return toResponse(repo.save(h));
    } // otel bilgilerini güncelleme

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NoSuchElementException("Otel bulunamadı.");
        repo.deleteById(id);
    } // otel silme
}
