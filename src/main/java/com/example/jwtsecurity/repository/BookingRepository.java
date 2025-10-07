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
    
    // tüm rezervasyonları bul
    Page<Booking> findByUser(User user, Pageable pageable);
    
    // kullanıcının rezervo
    List<Booking> findByUser(User user);
    
    // booking türüne göre 
    List<Booking> findByBookingType(Booking.BookingType bookingType);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    // kullanıcı ve bookin türüne göre
    List<Booking> findByUserAndBookingType(User user, Booking.BookingType bookingType);
    
    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);
    
    List<Booking> findByItemIdAndBookingType(Long itemId, Booking.BookingType bookingType);
    
    // son rezeer
    @Query("SELECT b FROM Booking b ORDER BY b.createdAt DESC")
    Page<Booking> findRecentBookings(Pageable pageable);
    
    // tarih aralığında
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
    List<Booking> findByBookingDateBetween(@Param("startDate") String startDate, 
                                          @Param("endDate") String endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserId(@Param("userId") Long userId);
}
