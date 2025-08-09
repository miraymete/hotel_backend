package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.HotelRequest;
import com.example.jwtsecurity.dto.HotelResponse;
import com.example.jwtsecurity.model.Hotel;
import com.example.jwtsecurity.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HotelService {

    private final HotelRepository repo;

    public HotelService(HotelRepository repo) { this.repo = repo; }

    private HotelResponse toResponse(Hotel h) {
        return new HotelResponse(h.getId(), h.getName(), h.getCity(), h.getStars());
    } // otel bilgilerini yanıt olarak döndürme

    public List<HotelResponse> listAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    } // otelleri listeleme

    public HotelResponse create(HotelRequest req) {
        Hotel h = new Hotel();
        h.setName(req.getName());
        h.setCity(req.getCity());
        h.setStars(req.getStars());
        repo.save(h);
        return toResponse(h);
    } // otel ekleme

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
        repo.save(h);
        return toResponse(h);
    } // otel bilgilerini güncelleme
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NoSuchElementException("Otel bulunamadı.");
        repo.deleteById(id);
    } // otel silme
}
