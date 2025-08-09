// src/main/java/com/example/jwtsecurity/dto/UpdateUserRequest.java
package com.example.jwtsecurity.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String role;
}
//// Bu sınıf, kullanıcı güncelleme isteğini temsil eder. ??