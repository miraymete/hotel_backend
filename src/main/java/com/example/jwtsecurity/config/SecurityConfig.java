package com.example.jwtsecurity.config;

import com.example.jwtsecurity.filter.JwtAuthFilter;
import com.example.jwtsecurity.service.CustomUserDetailsService;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// temel güvenlik kuralları ve cors ayarları
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                         JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // JwtAuthFilter component olarak yönetilir 

    // şifreleyici
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //custom kullanıcı servisi ile bağlanır
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        
        cfg.setAllowCredentials(true);
        
        cfg.setAllowedOriginPatterns(List.of(
            "https://*.vercel.app", 
            "http://localhost:*",    
            "http://127.0.0.1:*"   
        ));
        
        // izinli yöntemler
        cfg.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // izinli headerlar
        cfg.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type", 
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        
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

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, 
                             AccessDeniedException accessDeniedException) throws IOException {

                // structured logging with Log4j2
                logger.warn("Access Denied - Path: {}, Method: {}, Origin: {}, User-Agent: {}, Reason: {}", 
                    request.getRequestURI(), 
                    request.getMethod(), 
                    request.getHeader("Origin"), 
                    request.getHeader("User-Agent"), 
                    accessDeniedException.getMessage());
                
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Access Denied\",\"message\":\"" + 
                    accessDeniedException.getMessage() + "\",\"path\":\"" + 
                    request.getRequestURI() + "\",\"method\":\"" + request.getMethod() + "\"}");
            }
        };
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, 
                               AuthenticationException authException) throws IOException {

                // structured logging with Log4j2
                logger.error("Authentication Failed - Path: {}, Method: {}, Origin: {}, Reason: {}", 
                    request.getRequestURI(), 
                    request.getMethod(), 
                    request.getHeader("Origin"), 
                    authException.getMessage());
                
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
                        // public endpointler kimlik doğrulama istemez
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // admin endpointler sadece admin rolü
                        .requestMatchers("/api/bookings/all").hasRole("ADMIN")
                        .requestMatchers("/api/bookings/*/status").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        //kimliği doğrulanmış kullanıcı
                        .requestMatchers("/api/auth/account").authenticated()
                        .requestMatchers("/api/bookings/**").authenticated()
                        .requestMatchers("/api/hotels/**").authenticated()
                        .requestMatchers("/api/tours/**").authenticated()
                        .requestMatchers("/api/yachts/**").authenticated()
                        
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
