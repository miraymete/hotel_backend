package com.example.jwtsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")   // sadece ROLE_ADMIN erişebilir
    public String adminHello() {
        return "Merhaba Admin!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String userHello() {
        return "Merhaba User ya da Admin!";
    }
}
