// src/main/java/com/example/jwtsecurity/repository/UserRepository.java
package com.example.jwtsecurity.repository;

import com.example.jwtsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
// Bu arayüz, User modelini yönetmek için JpaRepository'yi genişletir. ?????????????