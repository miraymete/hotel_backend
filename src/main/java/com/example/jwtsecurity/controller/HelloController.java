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

    @GetMapping("/api/hello")
    public String apiHello() {
        return "API Hello - Backend çalışıyor";
    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userHello() {
        return "Merhaba user ya da admin";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminHello() {
        return "Merhaba admin";
    }
}
