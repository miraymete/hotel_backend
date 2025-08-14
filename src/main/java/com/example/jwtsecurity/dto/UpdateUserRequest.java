// src/main/java/com/example/jwtsecurity/dto/UpdateUserRequest.java
package com.example.jwtsecurity.dto;

public class UpdateUserRequest {

    private Long id;
    private String username;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

//// Bu sınıf, kullanıcı güncelleme isteğini temsil eder. ??