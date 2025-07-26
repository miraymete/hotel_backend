package com.example.jwtsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                //  csrf  tamamen kapat
                .csrf(AbstractHttpConfigurer::disable)

                // oturum tutma (jwt için)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // yetkilendirme
                .authorizeHttpRequests(auth -> auth
                        // register & login serbest (POST/GET farketmez)
                        .requestMatchers("/api/auth/**").permitAll()
                        // H2 consolea da izin verirsen:
                        //.requestMatchers("/h2-console/**").permitAll()
                        // diğer tüm istekler token ile
                        .anyRequest().authenticated()
                )

                // JWT filtresini yerleştir
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
