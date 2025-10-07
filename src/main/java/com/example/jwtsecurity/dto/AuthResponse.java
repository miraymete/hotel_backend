package com.example.jwtsecurity.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserResponseDto user;
    
    public AuthResponse() {}
    
    public AuthResponse(String token, UserResponseDto user) {
        this.token = token;
        this.user = user;
    }
}
