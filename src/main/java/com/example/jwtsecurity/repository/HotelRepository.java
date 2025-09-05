package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Son dakika kutusu
    Page<Hotel> findByLastMinuteTrue(Pageable pageable);

    // Basit arama (q, yıldız, fiyat) – amenities’i sonra ekleriz
    @Query("""
        select h from Hotel h
        where (:q is null or lower(h.name) like lower(concat('%', :q, '%'))
               or lower(h.city) like lower(concat('%', :q, '%'))
               or lower(h.region) like lower(concat('%', :q, '%'))
               or lower(h.country) like lower(concat('%', :q, '%')))
          and (:minStars is null or h.stars >= :minStars)
          and (:maxStars is null or h.stars <= :maxStars)
          and (:minPrice is null or h.pricePerNight >= :minPrice)
          and (:maxPrice is null or h.pricePerNight <= :maxPrice)
        """)
    Page<Hotel> search(@Param("q") String q,
                       @Param("minStars") Integer minStars,
                       @Param("maxStars") Integer maxStars,
                       @Param("minPrice") BigDecimal minPrice,
                       @Param("maxPrice") BigDecimal maxPrice,
                       Pageable pageable);

    Page<Hotel> findAllByOrderByRatingScoreDesc(Pageable pageable); // en iyiler
}
