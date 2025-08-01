package com.kaptue.educamer.config;


import com.kaptue.educamer.security.JwtAuthenticationEntryPoint; // Nouvelle classe pour gérer les erreurs 401
import com.kaptue.educamer.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // Importer
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importer
import java.util.Arrays; // Importer
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) 
public class SecurityConfig {

    @Value("${FRONTEND_URL}")
    private String frontendUrl;


    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Pour gérer les erreurs d'authentification

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour une API stateless
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Utiliser la configuration CORS bean
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Gérer les erreurs 401
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Endpoints d'authentification (login, register)
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permettre les requêtes OPTIONS pour CORS

                // La sécurité des endpoints spécifiques (Tickets, Devis, etc.)
                // sera gérée avec @PreAuthorize directement dans les contrôleurs.
                // Cela offre plus de flexibilité pour la logique basée sur les postes.
                // Exemples de règles globales si vous en aviez besoin (mais @PreAuthorize est mieux) :
                // .requestMatchers("/api/users/**").hasAuthority(Permission.USER_MANAGE)

                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Votre frontend Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Ou spécifiez les headers nécessaires
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer cette configuration à toutes les routes
        return source;
    }
}

