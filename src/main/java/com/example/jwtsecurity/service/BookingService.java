package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.BookingRequest;
import com.example.jwtsecurity.dto.BookingResponse;
import com.example.jwtsecurity.model.Booking;
import com.example.jwtsecurity.model.Hotel;
import com.example.jwtsecurity.model.Tour;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.model.Yacht;
import com.example.jwtsecurity.repository.BookingRepository;
import com.example.jwtsecurity.repository.HotelRepository;
import com.example.jwtsecurity.repository.TourRepository;
import com.example.jwtsecurity.repository.YachtRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final HotelRepository hotelRepository;
    private final TourRepository tourRepository;
    private final YachtRepository yachtRepository;

    public BookingService(BookingRepository bookingRepository, UserService userService,
                         HotelRepository hotelRepository, TourRepository tourRepository,
                         YachtRepository yachtRepository) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.hotelRepository = hotelRepository;
        this.tourRepository = tourRepository;
        this.yachtRepository = yachtRepository;
    }

    // Yeni rezervasyon oluştur
    public BookingResponse createBooking(BookingRequest request) {
        // Giriş yapmış kullanıcıyı bul
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Booking oluştur
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingType(request.getBookingType());
        booking.setItemId(request.getItemId());
        booking.setItemName(request.getItemName());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setBookingDate(request.getBookingDate());
        booking.setGuestCount(request.getGuestCount());
        booking.setNotes(request.getNotes());

        // Fiyatı hesapla
        BigDecimal totalPrice = calculatePrice(request.getBookingType(), request.getItemId(), request.getGuestCount());
        booking.setTotalPrice(totalPrice);

        // Kaydet
        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponse(savedBooking);
    }

    // Kullanıcının rezervasyonlarını getir
    public Page<BookingResponse> getUserBookings(int page, int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookings = bookingRepository.findByUser(user, pageable);
        return bookings.map(this::convertToResponse);
    }

    // Tüm rezervasyonları getir (Admin)
    public Page<BookingResponse> getAllBookings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookings = bookingRepository.findRecentBookings(pageable);
        return bookings.map(this::convertToResponse);
    }

    // Rezervasyon durumunu güncelle (Admin)
    public BookingResponse updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponse(updatedBooking);
    }

    // Rezervasyonu iptal et
    public BookingResponse cancelBooking(Long bookingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Sadece kendi rezervasyonunu iptal edebilir
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponse(updatedBooking);
    }

    // Fiyat hesaplama
    private BigDecimal calculatePrice(Booking.BookingType bookingType, Long itemId, Integer guestCount) {
        BigDecimal basePrice = BigDecimal.ZERO;

        switch (bookingType) {
            case HOTEL:
                Optional<Hotel> hotelOpt = hotelRepository.findById(itemId);
                if (hotelOpt.isPresent()) {
                    basePrice = hotelOpt.get().getPricePerNight();
                }
                break;
            case TOUR:
                Optional<Tour> tourOpt = tourRepository.findById(itemId);
                if (tourOpt.isPresent()) {
                    basePrice = tourOpt.get().getPrice();
                }
                break;
            case YACHT:
                Optional<Yacht> yachtOpt = yachtRepository.findById(itemId);
                if (yachtOpt.isPresent()) {
                    basePrice = yachtOpt.get().getPrice();
                }
                break;
        }

        return basePrice.multiply(BigDecimal.valueOf(guestCount));
    }

    // Booking entity'sini BookingResponse'a dönüştür
    private BookingResponse convertToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getUsername(),
                booking.getBookingType(),
                booking.getItemId(),
                booking.getItemName(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getBookingDate(),
                booking.getGuestCount(),
                booking.getTotalPrice(),
                booking.getCurrency(),
                booking.getStatus(),
                booking.getNotes(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }
}
