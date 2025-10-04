package com.example.jwtsecurity.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String role;
    
    // Default constructor
    public UserResponseDto() {}
    
    // All args constructor
    public UserResponseDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
