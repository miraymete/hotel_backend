package com.example.jwtsecurity.security;

import com.example.jwtsecurity.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpMethod;
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
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                         JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // JwtAuthFilter artık @Component ile yönetiliyor, duplicate bean riski ortadan kalktı

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

    // Global CORS konfigürasyonu - görsellerdeki tavsiyelere göre
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        
        // Axios withCredentials: true -> allowCredentials zorunlu
        cfg.setAllowCredentials(true);
        
        // Spesifik originler: vercel + local dev (wildcard yerine pattern)
        cfg.setAllowedOriginPatterns(List.of(
            "https://*.vercel.app",  // Tüm vercel subdomain'leri
            "http://localhost:*",    // Tüm localhost portları
            "http://127.0.0.1:*"    // Tüm 127.0.0.1 portları
        ));
        
        // İzinli yöntemler
        cfg.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // İzinli headerlar
        cfg.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type", 
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        
        // Tarayıcıdan okunacak headerlar
        cfg.setExposedHeaders(List.of(
            "Authorization"
        ));

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - kimlik doğrulama gerektirmez
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // Admin endpoints - sadece ADMIN rolü
                        .requestMatchers("/api/bookings/all").hasRole("ADMIN")
                        .requestMatchers("/api/bookings/*/status").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        
                        // User endpoints - authenticated users
                        .requestMatchers("/api/bookings/**").authenticated()
                        .requestMatchers("/api/hotels/**").authenticated()
                        .requestMatchers("/api/tours/**").authenticated()
                        .requestMatchers("/api/yachts/**").authenticated()
                        
                        // Diğer tüm istekler için kimlik doğrulama gerekli
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider(
                    userDetailsService,
                    passwordEncoder()
                ))
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }
}
