package com.example.jwtsecurity.service;

import com.example.jwtsecurity.dto.RegisterRequest;
import com.example.jwtsecurity.dto.UserResponseDto;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        // 1 email zaten var mı kontrol et
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor");
        }
        
        // 2 yeni bir user nesnesi oluştur
        User user = new User();
        // 3 setterlarla alanları doldur
        user.setUsername(request.getUsername()); // Email'den otomatik oluşturulur
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRoleOrDefault()); // default rol kullan
        
        // Profil bilgilerini ekle
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        
        // 4 kaydet
        userRepository.save(user);
    }

    // Kullanıcıyı ID ile bul
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Kullanıcıyı username ile bul
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Kullanıcıyı email ile bul
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Username veya email ile kullanıcı bul (login için)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        // Önce username ile dene
        Optional<User> userByUsername = userRepository.findByUsername(usernameOrEmail);
        if (userByUsername.isPresent()) {
            return userByUsername;
        }
        
        // Username bulunamazsa email ile dene
        return userRepository.findByEmail(usernameOrEmail);
    }

    // User'ı UserResponseDto'ya dönüştür
    public UserResponseDto convertToDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getFullName(),
            user.getPhoneNumber(),
            user.getDateOfBirth(),
            user.getProfileImageUrl(),
            user.getCreatedAt(),
            user.getIsActive(),
            user.getEmailVerified()
        );
    }
}
