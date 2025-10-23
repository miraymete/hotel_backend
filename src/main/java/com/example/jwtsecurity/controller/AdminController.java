package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.*;
import com.example.jwtsecurity.service.AdminService;
import com.example.jwtsecurity.model.Booking;
import com.example.jwtsecurity.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // genel bakış ve istatistikler
    
    /**
     * dashboard ana sayfası için kullanıcı sayısı rezervasyon sayısı son bir ay yapılan rezervasyonlar
     *  aktif otel sayısı ve son 7 günün rezervasyonları
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }


    // kullanıcı yönetimi endpointlei
    
    /**
     * kullanıcıları listeleme
     *  arama rol ve durum filtreleme 
     * filtreleme yapılabilecek sayfaya role göre falan 
     */
    @GetMapping("/users")
    public ResponseEntity<PageResponseDto<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        
        PageResponseDto<UserResponseDto> users = adminService.getAllUsers(page, size, search, role, status);
        return ResponseEntity.ok(users);
    }

    /**
     * herhangi bie kullanıcının bilgileri 
     * kullanıcı bilgileri, 
     * rezervasyon geçmişi ve son aktiviteleri 
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailDto> getUserDetails(@PathVariable Long id) {
        UserDetailDto user = adminService.getUserDetails(id);
        return ResponseEntity.ok(user);
    }

    // kullanıcıyı aktif veya psif yapma
    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<Map<String, String>> toggleUserStatus(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return ResponseEntity.ok(Map.of("message", "Kullanıcı n durumu güncellendi"));
    }

    // isim  mail rol telefon bunlar değiştirilebilie 
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, 
                                                     @Valid @RequestBody UpdateUserRequestDto request) {
        UserResponseDto updatedUser = adminService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // kullanıcıyı silme
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Kullanıcı silindi"));
    }

    // rezervasyon yönetimi endpointleri
    
    // aynı kullanıcı gibi rezervoları da idye tarihe typea role falan göre filtreleme 
    @GetMapping("/bookings")
    public ResponseEntity<PageResponseDto<BookingDetailDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long userId) {
        
        PageResponseDto<BookingDetailDto> bookings = adminService.getAllBookings(
            page, size, status, type, startDate, endDate, userId);
        return ResponseEntity.ok(bookings);
    }

    // rezervasyon bilgilerini getirmem
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDetailDto> getBookingDetails(@PathVariable Long id) {
        BookingDetailDto booking = adminService.getBookingDetails(id);
        return ResponseEntity.ok(booking);
    }

    // rezervasyon durumunu değiştirme iptal etme veya onaylama  
    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<BookingDetailDto> updateBookingStatus(@PathVariable Long id, 
                                                              @RequestParam String status) {
        BookingDetailDto updatedBooking = adminService.updateBookingStatus(id, status);
        return ResponseEntity.ok(updatedBooking);
    }

    // birden fazla rezervasyonun ayarını değiştirme 
    @PatchMapping("/bookings/bulk-status")
    public ResponseEntity<Map<String, String>> updateBulkBookingStatus(
            @RequestBody BulkStatusUpdateRequestDto request) {
        adminService.updateBulkBookingStatus(request);
        return ResponseEntity.ok(Map.of("message", "Toplu güncelleme tamamlandı"));
    }


    // sistem yönetimi endpointleri
    
    // sistem sağlığı ama içeriği nedir bilmiyorum
    @GetMapping("/system/health")
    public ResponseEntity<SystemHealthDto> getSystemHealth() {
        SystemHealthDto health = adminService.getSystemHealth();
        return ResponseEntity.ok(health);
    }

    // sistem logların. logların kayıdı ve levelleri
    @GetMapping("/system/logs")
    public ResponseEntity<List<SystemLogDto>> getSystemLogs(
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(required = false) String level) {
        List<SystemLogDto> logs = adminService.getSystemLogs(limit, level);
        return ResponseEntity.ok(logs);
    }
}
