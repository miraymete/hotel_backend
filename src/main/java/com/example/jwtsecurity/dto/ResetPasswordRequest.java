package com.example.jwtsecurity.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String newPassword;

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
// şifre sıfırlama isteği