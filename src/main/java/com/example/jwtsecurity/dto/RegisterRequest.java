package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// register request dto http isteğinden gelen veriyi taşır
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String role;

    // Profil bilgileri
    private String phoneNumber;
    private String dateOfBirth;

    public RegisterRequest() { }

    public RegisterRequest(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = "USER"; // Default role
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    // role alanı için default değer
    public String getRoleOrDefault() {
        return role != null ? role : "USER";
    }

    // Username'i email'den otomatik oluştur (JWT için gerekli)
    public String getUsername() {
        return email != null ? email.split("@")[0] : null;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
               "fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", password='[PROTECTED]'" +
               ", role='" + role + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", dateOfBirth='" + dateOfBirth + '\'' +
               '}';
    }
}