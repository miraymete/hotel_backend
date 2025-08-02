package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.RegisterRequest;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        // 1)yeni bir User nesnesi oluştur
        User user = new User();
        // 2) setterlarla alanları doldur
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        // 3) kaydet
        userRepository.save(user);
    }
}
