package com.example.jwtsecurity.service;

import com.example.jwtsecurity.model.Hotel;
import com.example.jwtsecurity.model.Room;
import com.example.jwtsecurity.repository.HotelRepository;
import com.example.jwtsecurity.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public List<Hotel> getAllActiveHotels() {
        return hotelRepository.findByIsActiveTrue();
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public List<Room> getHotelRooms(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public List<Room> getAvailableRooms(Long hotelId, Integer guestCount) {
        return roomRepository.findAvailableRoomsByHotelAndGuestCount(hotelId, guestCount);
    }

    public List<Hotel> searchHotels(String query, String city, String country, 
                                   java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        if (query != null && !query.trim().isEmpty()) {
            return hotelRepository.searchHotels(query);
        }
        
        if (city != null && !city.trim().isEmpty()) {
            return hotelRepository.findByCityAndIsActiveTrue(city);
        }
        
        if (country != null && !country.trim().isEmpty()) {
            return hotelRepository.findByCountryAndIsActiveTrue(country);
        }
        
        if (minPrice != null && maxPrice != null) {
            return hotelRepository.findByPriceRange(minPrice, maxPrice);
        }
        
        return getAllActiveHotels();
    }

    public List<String> getDistinctCities() {
        return hotelRepository.findDistinctCities();
    }

    public List<String> getDistinctCountries() {
        return hotelRepository.findDistinctCountries();
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel hotel) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        
        existingHotel.setName(hotel.getName());
        existingHotel.setDescription(hotel.getDescription());
        existingHotel.setLocation(hotel.getLocation());
        existingHotel.setAddress(hotel.getAddress());
        existingHotel.setCity(hotel.getCity());
        existingHotel.setCountry(hotel.getCountry());
        existingHotel.setRating(hotel.getRating());
        existingHotel.setStarCount(hotel.getStarCount());
        existingHotel.setBasePrice(hotel.getBasePrice());
        existingHotel.setCurrency(hotel.getCurrency());
        existingHotel.setImageUrls(hotel.getImageUrls());
        existingHotel.setAmenities(hotel.getAmenities());
        
        return hotelRepository.save(existingHotel);
    }

    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        
        hotel.setIsActive(false);
        hotelRepository.save(hotel);
    }
}