package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.RegisterRequest;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// user service kullanıcı kayıt ve basit iş mantığını barındırır
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

    // register yeni kullanıcı oluşturur ve parolayı güvenli şekilde saklar
    public void register(RegisterRequest request) {
        // 1 kullanıcı zaten var mı kontrol et
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }
        
        // 2 yeni bir user nesnesi oluştur
        User user = new User();
        // 3 setterlarla alanları doldur
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(request.getRoleOrDefault()); // default rol kullan
        // 4 kaydet
        userRepository.save(user);
    }
}
