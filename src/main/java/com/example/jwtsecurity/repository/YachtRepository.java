package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Yacht;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface YachtRepository extends JpaRepository<Yacht, Long> {
    
    // Kategoriye göre yatları bul
    Page<Yacht> findByCategory(String category, Pageable pageable);
    
    // Önerilen yatları bul
    List<Yacht> findByIsRecommendedTrue();
    
    // Arama sorgusu
    @Query("SELECT y FROM Yacht y WHERE " +
           "(:q IS NULL OR LOWER(y.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(y.description) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(y.location) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:minPrice IS NULL OR y.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR y.price <= :maxPrice)")
    Page<Yacht> searchYachts(@Param("q") String query, 
                            @Param("minPrice") BigDecimal minPrice, 
                            @Param("maxPrice") BigDecimal maxPrice, 
                            Pageable pageable);
    
    // Kategori ve fiyat aralığına göre arama
    @Query("SELECT y FROM Yacht y WHERE " +
           "(:category IS NULL OR y.category = :category) AND " +
           "(:minPrice IS NULL OR y.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR y.price <= :maxPrice)")
    Page<Yacht> findByCategoryAndPriceBetween(@Param("category") String category,
                                            @Param("minPrice") BigDecimal minPrice,
                                            @Param("maxPrice") BigDecimal maxPrice,
                                            Pageable pageable);
}
