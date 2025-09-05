package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.LoginRequest;
import com.example.jwtsecurity.dto.RegisterRequest;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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

    /**  LOGIN  */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {

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

        // frontende gönderilecek response objesi oluştur
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "role", user.getRole()
        ));

        return ResponseEntity.ok(response);
    }

    /**  REGISTER  */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        // yeni kullanıcıyı veritabanına kaydet
        userService.register(request);
        
        // kayıt sonrası otomatik giriş için jwt token oluştur
        String token = jwtUtil.generateToken(request.getUsername());
        
        // kayıt edilen kullanıcının bilgilerini veritabanından getir
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // frontend'e gönderilecek response objesi oluştur
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "role", user.getRole()
        ));

        return ResponseEntity.ok(response);
    }
}
