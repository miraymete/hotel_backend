package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.BookingRequest;
import com.example.jwtsecurity.dto.HotelBookingRequest;
import com.example.jwtsecurity.dto.TourBookingRequest;
import com.example.jwtsecurity.dto.YachtBookingRequest;
import com.example.jwtsecurity.model.Booking;
import com.example.jwtsecurity.model.Hotel;
import com.example.jwtsecurity.model.Room;
import com.example.jwtsecurity.model.Tour;
import com.example.jwtsecurity.model.Yacht;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.BookingRepository;
import com.example.jwtsecurity.repository.HotelRepository;
import com.example.jwtsecurity.repository.RoomRepository;
import com.example.jwtsecurity.repository.TourRepository;
import com.example.jwtsecurity.repository.YachtRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final TourRepository tourRepository;
    private final YachtRepository yachtRepository;

    public BookingService(BookingRepository bookingRepository,
                         HotelRepository hotelRepository,
                         RoomRepository roomRepository,
                         TourRepository tourRepository,
                         YachtRepository yachtRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.tourRepository = tourRepository;
        this.yachtRepository = yachtRepository;
    }

    public Booking createBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setBookingType(bookingRequest.getBookingType());
        booking.setItemId(bookingRequest.getItemId());
        booking.setItemName(bookingRequest.getItemName());
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setGuestCount(bookingRequest.getGuestCount());
        booking.setTotalPrice(bookingRequest.getTotalPrice());
        booking.setCurrency(bookingRequest.getCurrency());
        booking.setNotes(bookingRequest.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);

      

        return bookingRepository.save(booking);
    }

    public Booking createHotelBooking(HotelBookingRequest request, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + request.getRoomId()));

        // fiyat hesaplama
        BigDecimal totalPrice = room.getNightlyPrice().multiply(BigDecimal.valueOf(request.getNights()));

        Booking booking = new Booking();
        booking.setBookingType(Booking.BookingType.HOTEL);
        booking.setItemId(hotelId);
        booking.setItemName(hotel.getName());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setBookingDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        booking.setGuestCount(request.getGuestCount());
        booking.setTotalPrice(totalPrice);
        booking.setCurrency(room.getCurrency());
        booking.setNotes(request.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public Booking createTourBooking(TourBookingRequest request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + request.getTourId()));

        BigDecimal totalPrice = tour.getPrice().multiply(BigDecimal.valueOf(request.getParticipantCount()));

        Booking booking = new Booking();
        booking.setBookingType(Booking.BookingType.TOUR);
        booking.setItemId(request.getTourId());
        booking.setItemName(tour.getName());
        booking.setCheckInDate(request.getTourDate());
        booking.setBookingDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        booking.setGuestCount(request.getParticipantCount());
        booking.setTotalPrice(totalPrice);
        booking.setCurrency(tour.getCurrency());
        booking.setNotes(request.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public Booking createYachtBooking(YachtBookingRequest request) {
        Yacht yacht = yachtRepository.findById(request.getYachtId())
                .orElseThrow(() -> new RuntimeException("Yacht not found with id: " + request.getYachtId()));

        BigDecimal totalPrice = yacht.getBasePrice().multiply(BigDecimal.valueOf(request.getDurationHours()));

        Booking booking = new Booking();
        booking.setBookingType(Booking.BookingType.YACHT);
        booking.setItemId(request.getYachtId());
        booking.setItemName(yacht.getName());
        booking.setCheckInDate(request.getDepartureDate());
        booking.setBookingDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        booking.setGuestCount(request.getGuestCount());
        booking.setTotalPrice(totalPrice);
        booking.setCurrency(yacht.getCurrency());
        booking.setNotes(request.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setGuestCount(request.getGuestCount());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setNotes(request.getNotes());

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            booking.setStatus(bookingStatus);
            return bookingRepository.save(booking);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid booking status: " + status);
        }
    }

    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
}