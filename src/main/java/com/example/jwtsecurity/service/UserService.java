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

    public void register(RegisterRequest request) {
        // email kontorl
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor");
        }
        
        User user = new User();
        user.setUsername(request.getUsername()); // Email'den otomatik oluşturulur
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRoleOrDefault()); // default rol kullan
        
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        // önce username ile
        Optional<User> userByUsername = userRepository.findByUsername(usernameOrEmail);
        if (userByUsername.isPresent()) {
            return userByUsername;
        }
        
        return userRepository.findByEmail(usernameOrEmail);
    }

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
