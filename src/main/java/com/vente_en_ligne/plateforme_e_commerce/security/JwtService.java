package com.vente_en_ligne.plateforme_e_commerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // 🔐 Clé secrète pour signer les JWT (24+ caractères minimum)
    private static final String SECRET_KEY =
            "my-super-secret-key-for-jwt-authentication-2026-secure";

    // ✅ Clé de signature HMAC
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // 🔹 Générer un token avec username et rôle
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        // Ajouter ROLE_ si nécessaire
        String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        claims.put("role", formattedRole);

        System.out.println("=== JWT GENERATION ===");
        System.out.println("Email: " + email);
        System.out.println("Role envoyé: " + formattedRole);

        return generateToken(claims, email);
    }

    // 🔹 Générer un token simple (sans rôle)
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    // 🔹 Génération de token principal avec claims personnalisés
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Extraire username (subject) du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 🔹 Extraire rôle du token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // 🔹 Extraire un claim générique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🔹 Extraire la date d’expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔹 Vérifier si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🔹 Vérifier la validité du token pour un username
    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername != null &&
                    extractedUsername.equals(username) &&
                    !isTokenExpired(token);
        } catch (JwtException e) {
            System.out.println("❌ Token invalide: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Extraire tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .setAllowedClockSkewSeconds(60) // tolérance 60s pour l’horloge
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}