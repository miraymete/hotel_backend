// src/main/java/com/example/jwtsecurity/service/AdminUserService.java
package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.*;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> list() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getRole()))
                .toList();
    }

    public UserResponse create(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("kullanıcı adı zaten var");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole((req.getRole() == null || req.getRole().isBlank()) ? "USER" : req.getRole().toUpperCase());

        u = userRepository.save(u);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }

    public UserResponse get(Long id) {
        User u = userRepository.findById(id).orElseThrow();
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }

    public UserResponse update(Long id, UpdateUserRequest req) {
        User u = userRepository.findById(id).orElseThrow();

        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            u.setUsername(req.getUsername());
        }
        if (req.getRole() != null && !req.getRole().isBlank()) {
            u.setRole(req.getRole().toUpperCase());
        }

        u = userRepository.save(u);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("kullanıcı bulunamadı");
        }
        userRepository.deleteById(id);
    }

    public void resetPassword(Long id, String newPassword) {
        User u = userRepository.findById(id).orElseThrow();
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }
}
//yönetici kullanıcı işlemleri listeleme oluşturma güncelleme silme