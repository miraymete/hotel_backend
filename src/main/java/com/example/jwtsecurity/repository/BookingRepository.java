package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Booking;
import com.example.jwtsecurity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Kullanıcının tüm rezervasyonlarını bul
    Page<Booking> findByUser(User user, Pageable pageable);
    
    // Kullanıcının rezervasyonlarını bul
    List<Booking> findByUser(User user);
    
    // Booking türüne göre rezervasyonları bul
    List<Booking> findByBookingType(Booking.BookingType bookingType);
    
    // Booking durumuna göre rezervasyonları bul
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    // Kullanıcı ve booking türüne göre rezervasyonları bul
    List<Booking> findByUserAndBookingType(User user, Booking.BookingType bookingType);
    
    // Kullanıcı ve duruma göre rezervasyonları bul
    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);
    
    // Item ID ve booking türüne göre rezervasyonları bul
    List<Booking> findByItemIdAndBookingType(Long itemId, Booking.BookingType bookingType);
    
    // Son rezervasyonları bul (Admin için)
    @Query("SELECT b FROM Booking b ORDER BY b.createdAt DESC")
    Page<Booking> findRecentBookings(Pageable pageable);
    
    // Belirli tarih aralığındaki rezervasyonları bul
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
    List<Booking> findByBookingDateBetween(@Param("startDate") String startDate, 
                                          @Param("endDate") String endDate);
}
