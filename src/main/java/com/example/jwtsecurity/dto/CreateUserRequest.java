// src/main/java/com/example/jwtsecurity/dto/CreateUserRequest.java
package com.example.jwtsecurity.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    // gelmezse USER
    private String role;
}
// Bu sınıf, yeni kullanıcı oluşturma isteğini temsil eder. ??