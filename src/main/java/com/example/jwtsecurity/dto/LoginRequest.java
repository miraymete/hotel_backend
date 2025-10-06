package com.example.jwtsecurity.dto;

import jakarta.validation.constraints.NotBlank;

/**  Login isteği için DTO - hem username hem email ile login destekler  */
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    private String username; // Bu alan username veya email olabilir

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest() { }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
