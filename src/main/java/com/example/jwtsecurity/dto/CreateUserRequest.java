package com.example.jwtsecurity.dto;


import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    // gelmezse USER
    private String role;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
// Bu sınıf, yeni kullanıcı oluşturma isteğini temsil eder. ??