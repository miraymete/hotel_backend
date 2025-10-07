package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface YachtRepository extends JpaRepository<Yacht, Long> {
    
    List<Yacht> findByIsActiveTrue();
    
    List<Yacht> findByTypeAndIsActiveTrue(String type);
    
    List<Yacht> findByLocationContainingIgnoreCaseAndIsActiveTrue(String location);
    
    List<Yacht> findByBasePriceBetweenAndIsActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Yacht> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    
    @Query("SELECT y FROM Yacht y WHERE y.isActive = true AND " +
           "(LOWER(y.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(y.location) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Yacht> searchYachts(@Param("search") String search);
    
    @Query("SELECT DISTINCT y.type FROM Yacht y WHERE y.isActive = true ORDER BY y.type")
    List<String> findDistinctTypes();
    
    @Query("SELECT DISTINCT y.location FROM Yacht y WHERE y.isActive = true ORDER BY y.location")
    List<String> findDistinctLocations();
}