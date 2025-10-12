package com.example.jwtsecurity.filter;

import com.example.jwtsecurity.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// jwt auth filter giren isteklerde bearer token kontrolü yapar ve auth context kurar
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final String secretKey;

    public JwtAuthFilter(CustomUserDetailsService userDetailsService, 
                        @Value("${jwt.secret}") String secretKey) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String requestURI = request.getRequestURI();
        // debug için method
        System.out.println("JWT Filter - Path: " + path + ", URI: " + requestURI + ", Method: " + request.getMethod());
        
        // sadece login ve register endpointleri permitAll
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register") || 
            requestURI.equals("/api/auth/login") || requestURI.equals("/api/auth/register") ||
            path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") ||
            requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            // auth dışı yollar filtreyi atlar
            System.out.println("JWT Filter - Allowing path: " + path + " (URI: " + requestURI + ")");
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            String username = claims.getSubject();

            var userDetails = userDetailsService.loadUserByUsername(username);
            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
