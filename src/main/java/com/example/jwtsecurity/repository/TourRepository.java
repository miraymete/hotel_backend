package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    
    Page<Tour> findByCategory(String category, Pageable pageable);
    
    List<Tour> findByIsRecommendedTrue();
    
    //arama sorgusu
    @Query("SELECT t FROM Tour t WHERE " +
           "(:q IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(t.location) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR t.price <= :maxPrice)")
    Page<Tour> searchTours(@Param("q") String query, 
                          @Param("minPrice") BigDecimal minPrice, 
                          @Param("maxPrice") BigDecimal maxPrice, 
                          Pageable pageable);
    
    
    @Query("SELECT t FROM Tour t WHERE " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR t.price <= :maxPrice)")
    Page<Tour> findByCategoryAndPriceBetween(@Param("category") String category,
                                           @Param("minPrice") BigDecimal minPrice,
                                           @Param("maxPrice") BigDecimal maxPrice,
                                           Pageable pageable);
}
