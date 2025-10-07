package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    List<Room> findByHotelIdAndIsAvailableTrue(Long hotelId);
    
    List<Room> findByHotelId(Long hotelId);
    
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.isAvailable = true AND r.maxOccupancy >= :guestCount")
    List<Room> findAvailableRoomsByHotelAndGuestCount(@Param("hotelId") Long hotelId, 
                                                     @Param("guestCount") Integer guestCount);
    
    @Query("SELECT DISTINCT r.roomType FROM Room r WHERE r.hotel.id = :hotelId")
    List<String> findDistinctRoomTypesByHotel(@Param("hotelId") Long hotelId);
}
