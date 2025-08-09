// src/main/java/com/example/jwtsecurity/dto/UserResponse.java
package com.example.jwtsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String role;
}
// Bu sınıf, kullanıcı bilgilerini içeren bir yanıt nesnesidir ???