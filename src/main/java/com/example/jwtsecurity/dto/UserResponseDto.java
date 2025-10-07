package com.example.jwtsecurity.dto;

import java.time.LocalDateTime;

public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    
    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    private String profileImageUrl;
    
    private LocalDateTime createdAt;
    private Boolean isActive;
    private Boolean emailVerified;
    
    public UserResponseDto() {}
    
    public UserResponseDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
    
    public UserResponseDto(Long id, String username, String email, String role, 
                          String fullName, String phoneNumber, 
                          String dateOfBirth, String profileImageUrl, 
                          LocalDateTime createdAt, Boolean isActive, Boolean emailVerified)
                           {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.emailVerified = emailVerified;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; 
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; 
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth;
     }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; 
    }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
}