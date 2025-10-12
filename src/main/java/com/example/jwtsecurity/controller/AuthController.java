package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.LoginRequest;
import com.example.jwtsecurity.dto.RegisterRequest;
import com.example.jwtsecurity.dto.AuthResponse;
import com.example.jwtsecurity.dto.UserResponseDto;
import com.example.jwtsecurity.model.User;
import com.example.jwtsecurity.repository.UserRepository;
import com.example.jwtsecurity.utils.JwtUtil;
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

// kimlik doğrulama kayıt işlemleri 
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

    // login ve register için dto kullanılır 
    // başarıda authresponse döner hatada sade json

    // spring security authentication manager
    private final AuthenticationManager authenticationManager;
    // kullanıcı işlemleri  service
    private final UserService userService;
    // jwt token üretmek için util
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

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {

        // username ya da  mail ile kullanıcıyı bul
        Optional<User> userOpt = userService.findByUsernameOrEmail(loginRequest.getUsername());
        User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

 
        Authentication authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        user.getUsername(), // JWT için username kullan
                        loginRequest.getPassword()
                );

        //kullanıcı doğrulaması
        authenticationManager.authenticate(authRequest);

        //başarılı ise jwt token 
        String token = jwtUtil.generateToken(user.getUsername());

   
        UserResponseDto userDto = userService.convertToDto(user);
        
        // response oluşturfrontende gönder
        AuthResponse authResponse = new AuthResponse(token, userDto);

        return ResponseEntity.ok(authResponse);
    }


    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        try {
            System.out.println("kayıt debug");
            System.out.println("Request: " + request);
            System.out.println("FullName: " + request.getFullName());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Password: " + (request.getPassword() != null ? "[PROVIDED]" : "[NULL]"));
            System.out.println("Username (auto-generated): " + request.getUsername());
            System.out.println("Role: " + request.getRole());
            
            //eni kullanıcıyı veritabanına kaydet
            userService.register(request);
            
            //otomatik giriş için jwt token üret
            String token = jwtUtil.generateToken(request.getUsername());
            
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
            User user = userOpt.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            UserResponseDto userDto = userService.convertToDto(user);
            
            AuthResponse authResponse = new AuthResponse(token, userDto);

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // jwt auth test endpointi
               @GetMapping(value = "/test", produces = "application/json")
               public ResponseEntity<Map<String, Object>> testEndpoint(Authentication authentication) {
                   Map<String, Object> response = new HashMap<>();
                   response.put("message", "JWT Authentication successful!");
                   response.put("authenticated", authentication.isAuthenticated());
                   response.put("username", authentication.getName());
                   response.put("authorities", authentication.getAuthorities());
                   return ResponseEntity.ok(response);
               }

               @GetMapping(value = "/account", produces = "application/json")
               public ResponseEntity<Object> getAccountInfo(Authentication authentication) {
                   try {
                       String username = authentication.getName();
                       Optional<User> userOpt = userRepository.findByUsername(username);
                       
                       if (userOpt.isEmpty()) {
                           Map<String, Object> errorResponse = new HashMap<>();
                           errorResponse.put("error", true);
                           errorResponse.put("message", "User not found");
                           return ResponseEntity.notFound().build();
                       }
                       
                       User user = userOpt.get();
                       UserResponseDto userDto = userService.convertToDto(user);
                       
                       return ResponseEntity.ok(userDto);
                   } catch (Exception e) {
                       Map<String, Object> errorResponse = new HashMap<>();
                       errorResponse.put("error", true);
                       errorResponse.put("message", e.getMessage());
                       return ResponseEntity.badRequest().body(errorResponse);
                   }
               }
}
