package com.example.jwtsecurity.controller;

import com.example.jwtsecurity.dto.LoginRequest;
import com.example.jwtsecurity.dto.RegisterRequest;
import com.example.jwtsecurity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService           = userService;
    }

    /**  LOGIN  */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {

        Authentication authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );

        authenticationManager.authenticate(authRequest);     // doğrulama

        return ResponseEntity.ok().build();                  // başarılı ise 200 döner
    }

    /**  REGISTER  */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }
}
