package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.CreateUserRequest;
import com.example.jwtsecurity.dto.UpdateUserRequest;
import com.example.jwtsecurity.dto.ResetPasswordRequest;
import com.example.jwtsecurity.dto.UserResponse;
import com.example.jwtsecurity.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AdminUserService service;

    // MANUAL CONSTRUCTOR (Lombok yok)
    public UserController(AdminUserService service) {
        this.service = service;
    }

    // 1) Listeleme - sadece ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> list() {
        return service.list();
    }

    // 2) Oluşturma - sadece ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(@RequestBody CreateUserRequest req) {
        return service.create(req);
    }

    // 3) Tek kullanıcı getir - sadece ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // 4) Güncelleme - sadece ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse update(@PathVariable Long id,
                               @RequestBody UpdateUserRequest req) {
        return service.update(id, req);
    }

    // 5) Silme - sadece ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 6) Şifre sıfırlama - sadece ADMIN
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable Long id,
                                           @RequestBody ResetPasswordRequest req) {
        service.resetPassword(id, req.getNewPassword());
        return ResponseEntity.ok().body(
                java.util.Collections.singletonMap("message", "Şifre başarıyla güncellendi.")
        );
    }
}
