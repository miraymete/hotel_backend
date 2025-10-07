package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.*;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse create(CreateUserRequest req) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole() == null ? "USER" : req.getRole());
        return toResponse(userRepository.save(u));
    }

    public UserResponse getById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return toResponse(u);
    }

    public UserResponse update(Long id, UpdateUserRequest req) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (req.getUsername() != null) u.setUsername(req.getUsername());
        if (req.getRole() != null) u.setRole(req.getRole());
        return toResponse(userRepository.save(u));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void resetPassword(Long id, String newPassword) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    private UserResponse toResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setRole(u.getRole());
        return r;
    }
}

//yönetici kullanıcı işlemleri listeleme oluşturma güncelleme silme