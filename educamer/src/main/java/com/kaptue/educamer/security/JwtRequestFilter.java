package com.kaptue.educamer.security;

import com.kaptue.educamer.jwt.JwtUtil;
import com.kaptue.educamer.service.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter; // <-- UTILISER CELUI-CI

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter { // <-- HÉRITER DE OncePerRequestFilter

    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                // Si le token est blacklisté, on ne fait rien, la requête sera rejetée.
                chain.doFilter(request, response);
                return;
            }

            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println("Le token JWT a expiré");
            } catch (Exception e) {
                System.err.println("Erreur lors du parsing du JWT: " + e.getMessage());
            }
        }

        // Si on a un username et qu'il n'y a pas déjà une authentification dans le contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Si le token est valide, on configure la sécurité de Spring
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Après avoir configuré l'authentification, Spring SAIT que l'utilisateur est authentifié.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        chain.doFilter(request, response);
    }
}