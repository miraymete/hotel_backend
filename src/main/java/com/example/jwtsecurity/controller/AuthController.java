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
    "http://127.0.0.1:5174"
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

        // kullanıcı adı ve şifre ile authentication token oluştur
        Authentication authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );

        // spring security ile kullanıcı doğrulaması yap
        authenticationManager.authenticate(authRequest);

        // doğrulama başarılı ise jwt token oluştur
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        
        // veritabanından kullanıcı bilgilerini getir
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // UserResponseDto oluştur
        UserResponseDto userDto = new UserResponseDto(user.getId(), user.getUsername(), user.getRole());
        
        // auth response oluştur ve frontende gönder
        AuthResponse authResponse = new AuthResponse(token, userDto);

        return ResponseEntity.ok(authResponse);
    }

    // register endpoint yeni kullanıcı oluşturur ve jwt ile birlikte döner
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        try {
            // yeni kullanıcıyı veritabanına kaydet
            userService.register(request);
            
            // kayıt sonrası otomatik giriş için jwt token oluştur
            String token = jwtUtil.generateToken(request.getUsername());
            
            // kayıt edilen kullanıcının bilgilerini veritabanından getir
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
            User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            // UserResponseDto oluştur
            UserResponseDto userDto = new UserResponseDto(user.getId(), user.getUsername(), user.getRole());
            
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
}
