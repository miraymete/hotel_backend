package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.LoginRequest;
import com.example.jwtsecurity.dto.RegisterRequest;
import com.example.jwtsecurity.dto.AuthResponse;
import com.example.jwtsecurity.dto.UserResponseDto;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import com.example.jwtsecurity.security.JwtUtil;
import com.example.jwtsecurity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// auth controller jwt tabanlı giriş ve kayıt işlemleri
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173", 
    "http://localhost:5174",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:5174",
    "https://hotel-frontend-ts-zsjq.vercel.app"
}, allowCredentials = "true")
public class AuthController {

    // bu controller login ve register için dto kullanır ve class tabanlı cevap döndürür
    // success durumunda authresponse döndürülür error durumunda json map döndürülür

    // spring security authentication manager
    private final AuthenticationManager authenticationManager;
    // kullanıcı işlemleri için service
    private final UserService userService;
    // jwt token oluşturmak için utility
    private final JwtUtil jwtUtil;
    // veritabanı işlemleri için repository
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // login endpoint istek gövdesinden loginrequest alır doğrular ve jwt üretir
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {

        // Username veya email ile kullanıcıyı bul
        Optional<User> userOpt = userService.findByUsernameOrEmail(loginRequest.getUsername());
        User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // kullanıcı adı ve şifre ile authentication token oluştur
        Authentication authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        user.getUsername(), // JWT için username kullan
                        loginRequest.getPassword()
                );

        // spring security ile kullanıcı doğrulaması yap
        authenticationManager.authenticate(authRequest);

        // doğrulama başarılı ise jwt token oluştur
        String token = jwtUtil.generateToken(user.getUsername());

        // UserResponseDto oluştur (tüm profil bilgileri ile)
        UserResponseDto userDto = userService.convertToDto(user);
        
        // auth response oluştur ve frontende gönder
        AuthResponse authResponse = new AuthResponse(token, userDto);

        return ResponseEntity.ok(authResponse);
    }

    // register endpoint yeni kullanıcı oluşturur ve jwt ile birlikte döner
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        try {
            System.out.println("=== REGISTER DEBUG ===");
            System.out.println("Request: " + request);
            System.out.println("FullName: " + request.getFullName());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Password: " + (request.getPassword() != null ? "[PROVIDED]" : "[NULL]"));
            System.out.println("Username (auto-generated): " + request.getUsername());
            System.out.println("Role: " + request.getRole());
            
            // yeni kullanıcıyı veritabanına kaydet
            userService.register(request);
            
            // kayıt sonrası otomatik giriş için jwt token oluştur
            String token = jwtUtil.generateToken(request.getUsername());
            
            // kayıt edilen kullanıcının bilgilerini veritabanından getir
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
            User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            // UserResponseDto oluştur (tüm profil bilgileri ile)
            UserResponseDto userDto = userService.convertToDto(user);
            
            // auth response oluştur ve frontende gönder
            AuthResponse authResponse = new AuthResponse(token, userDto);

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // hata durumunda sade bir hata jsonu döndür
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Test endpoint - JWT authentication test için
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "JWT Authentication successful!");
        response.put("authenticated", authentication.isAuthenticated());
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        return ResponseEntity.ok(response);
    }
}
