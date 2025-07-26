package com.example.jwtsecurity.model;

import jakarta.persistence.*;
import lombok.*;

@Entity  // veritabanı tablosu demek
@Data     // getter setter otomatik gelir
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
}
