package com.example.jwtsecurity.security;

import com.example.jwtsecurity.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


// security config temel güvenlik kurallarını ve cors ayarlarını barındırır
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // jwt filtresi uygulama seviyesinde kimlik doğrulama için kullanılır
    @Bean
    public JwtAuthFilter jwtAuthFilter(CustomUserDetailsService userDetailsService) {
        return new JwtAuthFilter(userDetailsService);
    }

    // şiifreleyici
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // UserDetailsService zzaten CustomUserDetailsService
    // bağlama
    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // cors ayarları geliştirme ortamındaki frontendlere izin verir
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:5173",
            "http://localhost:5174",
            "http://127.0.0.1:5173",
            "http://127.0.0.1:5174",
            "https://hotel-frontend-ts.vercel.app",
            "https://hotel-frontend-luxop0lni-miraymetes-projects.vercel.app",
            "https://hotel-frontend-9j6i9nkpq-miraymetes-projects.vercel.app"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-XSRF-TOKEN", "X-CSRF-Token"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L); // bir saatlik preflight cache

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // access denied handler json hata döndürür ve sebebi loglar
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, 
                             AccessDeniedException accessDeniedException) throws IOException {
                
                // Log the access denied details
                System.err.println("=== ACCESS DENIED ===");
                System.err.println("Path: " + request.getRequestURI());
                System.err.println("Method: " + request.getMethod());
                System.err.println("Origin: " + request.getHeader("Origin"));
                System.err.println("User-Agent: " + request.getHeader("User-Agent"));
                System.err.println("Reason: " + accessDeniedException.getMessage());
                System.err.println("=====================");
                
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Access Denied\",\"message\":\"" + 
                    accessDeniedException.getMessage() + "\",\"path\":\"" + 
                    request.getRequestURI() + "\",\"method\":\"" + request.getMethod() + "\"}");
            }
        };
    }

    // authentication entry point yetkisiz erişimde json hata döndürür
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, 
                               AuthenticationException authException) throws IOException {
                
                // Log the authentication failure details
                System.err.println("=== AUTHENTICATION FAILED ===");
                System.err.println("Path: " + request.getRequestURI());
                System.err.println("Method: " + request.getMethod());
                System.err.println("Origin: " + request.getHeader("Origin"));
                System.err.println("Reason: " + authException.getMessage());
                System.err.println("=============================");
                
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"" + 
                    authException.getMessage() + "\",\"path\":\"" + 
                    request.getRequestURI() + "\",\"method\":\"" + request.getMethod() + "\"}");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter,
            AuthenticationProvider authenticationProvider
    ) throws Exception {

        // geliştirme için basitleştirilmiş güvenlik zinciri
        http
                .csrf(csrf -> csrf.disable()) // jwt ile stateful csrf gerekmez
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // tüm istekleri geçici olarak serbest bırak
                )
                .authenticationProvider(authenticationProvider)
                // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // jwt filtresini geçici olarak kapalı tuttuk
                .exceptionHandling(ex -> ex
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationEntryPoint()));

        return http.build();
    }
}
