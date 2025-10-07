package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    List<Hotel> findByIsActiveTrue();
    
    List<Hotel> findByLocationContainingIgnoreCaseAndIsActiveTrue(String location);
    
    List<Hotel> findByCityAndIsActiveTrue(String city);
    
    List<Hotel> findByCountryAndIsActiveTrue(String country);
    
    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND " +
           "(LOWER(h.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.location) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.city) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Hotel> searchHotels(@Param("search") String search);
    
    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND h.basePrice BETWEEN :minPrice AND :maxPrice")
    List<Hotel> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    @Query("SELECT DISTINCT h.city FROM Hotel h WHERE h.isActive = true ORDER BY h.city")
    List<String> findDistinctCities();
    
    @Query("SELECT DISTINCT h.country FROM Hotel h WHERE h.isActive = true ORDER BY h.country")
    List<String> findDistinctCountries();
}