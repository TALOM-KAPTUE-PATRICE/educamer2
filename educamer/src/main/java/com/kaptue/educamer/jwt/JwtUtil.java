package com.kaptue.educamer.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails; // Import recommandé

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {

    // --- Clés pour les informations personnalisées dans le token (Claims) ---
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_PERMISSIONS = "permissions";
    private static final String CLAIM_KEY_IS_RESET_TOKEN = "is_reset";

    // --- Durées d'expiration ---
    private final long EXPIRATION_TIME_AUTH_MS = 1000 * 60 * 60 * 24; // 24 heures
    private final long EXPIRATION_TIME_RESET_MS = 1000 * 60 * 15;   // 15 minutes

    // --- Injection de la clé secrète depuis application.properties ---
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    /**
     * Méthode privée pour générer la clé de signature à partir de la chaîne secrète.
     * La clé secrète doit être encodée en Base64.
     * C'est la méthode moderne et sécurisée recommandée par la bibliothèque jjwt.
     * @return Une clé de type java.security.Key prête pour la signature.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ========================================================================
    // --- MÉTHODES DE GÉNÉRATION DE TOKEN ---
    // ========================================================================

    /**
     * Génère un token d'authentification standard pour un utilisateur.
     *
     * @param userDetails Les détails de l'utilisateur fournis par Spring Security.
     * @param role Le rôle principal de l'utilisateur (ex: "ELEVE").
     * @param permissions L'ensemble des permissions granulaires.
     * @return Le token JWT sous forme de chaîne de caractères.
     */
    public String generateToken(UserDetails userDetails, String role, Set<String> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ROLE, role);
        claims.put(CLAIM_KEY_PERMISSIONS, permissions);
        
        return createToken(claims, userDetails.getUsername(), EXPIRATION_TIME_AUTH_MS);
    }
    
    /**
     * Génère un token spécifique pour la réinitialisation du mot de passe.
     * Ce token a une durée de vie plus courte et ne doit pas être utilisé pour l'authentification.
     *
     * @param email L'email de l'utilisateur qui réinitialise son mot de passe.
     * @return Le token JWT de réinitialisation.
     */
    public String generatePasswordResetToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_IS_RESET_TOKEN, true); // Marqueur spécial
        
        return createToken(claims, email, EXPIRATION_TIME_RESET_MS);
    }
    
    /**
     * Méthode centrale pour la création de n'importe quel type de token.
     *
     * @param claims Les informations personnalisées à inclure.
     * @param subject Le sujet du token (généralement l'email de l'utilisateur).
     * @param expirationMs La durée de validité du token en millisecondes.
     * @return Le token JWT compacté.
     */
    private String createToken(Map<String, Object> claims, String subject, long expirationMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // ========================================================================
    // --- MÉTHODES DE VALIDATION DE TOKEN ---
    // ========================================================================

    /**
     * Valide un token d'authentification.
     * Vérifie que le token correspond bien à l'utilisateur, qu'il n'est pas expiré
     * et qu'il ne s'agit pas d'un token de réinitialisation de mot de passe.
     *
     * @param token Le token JWT à valider.
     * @param userDetails Les détails de l'utilisateur à comparer.
     * @return true si le token est valide pour l'authentification, false sinon.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isPasswordResetToken(token));
    }


    // ========================================================================
    // --- MÉTHODES D'EXTRACTION DE DONNÉES (CLAIMS) ---
    // ========================================================================

    /**
     * Extrait le nom d'utilisateur (l'email) du token.
     * C'est un alias pour l'extraction du "subject".
     *
     * @param token Le token JWT.
     * @return L'email de l'utilisateur.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    /**
     * Extrait la date d'expiration du token.
     *
     * @param token Le token JWT.
     * @return La date d'expiration.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait le rôle de l'utilisateur du token.
     *
     * @param token Le token JWT.
     * @return Le rôle sous forme de chaîne de caractères.
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_KEY_ROLE, String.class));
    }
    
    /**
     * Extrait la liste des permissions du token.
     *
     * @param token Le token JWT.
     * @return Une liste de chaînes représentant les permissions.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_KEY_PERMISSIONS, List.class));
    }

    /**
     * Vérifie si un token est un token de réinitialisation de mot de passe.
     *
     * @param token Le token JWT.
     * @return true s'il s'agit d'un token de reset, false sinon.
     */
    public boolean isPasswordResetToken(String token) {
        try {
            return extractClaim(token, claims -> claims.get(CLAIM_KEY_IS_RESET_TOKEN, Boolean.class) != null);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Vérifie si le token est expiré.
     *
     * @param token Le token JWT.
     * @return true si le token est expiré, false sinon.
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            // Si une exception se produit (token malformé, etc.), on le considère comme expiré.
            return true;
        }
    }
    
    /**
     * Méthode générique pour extraire n'importe quelle information (claim) d'un token.
     * Utilise une expression lambda pour spécifier quelle information extraire.
     *
     * @param token Le token JWT.
     * @param claimsResolver Une fonction qui prend les Claims et retourne l'information désirée (T).
     * @return L'information extraite.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Méthode privée qui parse le token et retourne l'ensemble des claims.
     * C'est le point d'entrée pour toute lecture de token.
     * Gère les exceptions si le token est invalide.
     *
     * @param token Le token JWT à parser.
     * @return L'objet Claims contenant toutes les informations du token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}