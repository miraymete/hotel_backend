// src/main/java/com/example/jwtsecurity/dto/ResetPasswordRequest.java
package com.example.jwtsecurity.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String newPassword;

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
// Bu sınıf, şifre sıfırlama isteğini temsil eder.