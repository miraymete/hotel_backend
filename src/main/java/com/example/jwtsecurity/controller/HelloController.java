package com.example.jwtsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Herkese açık endpoint";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // user ve admin girebilir
    public String userHello() {
        return "Merhaba User ya da Admin!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // Sadece admin girebilir
    public String adminHello() {
        return "Merhaba Admin!";
    }
}
